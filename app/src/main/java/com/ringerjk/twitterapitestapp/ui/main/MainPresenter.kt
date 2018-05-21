package com.ringerjk.twitterapitestapp.ui.main

import com.ringerjk.twitterapitestapp.model.Twit
import com.ringerjk.twitterapitestapp.model.TwitOption
import com.ringerjk.twitterapitestapp.model.base.FieldValidationException
import com.ringerjk.twitterapitestapp.network.NetworkError
import com.ringerjk.twitterapitestapp.repository.StreamRepository
import com.ringerjk.twitterapitestapp.ui.base.BasePresenter
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import retrofit2.HttpException
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

open class MainPresenter @Inject constructor(
        private val streamRepository: StreamRepository
) : BasePresenter<MainContract.View>(), MainContract.Presenter {

    open var twitsSubject: PublishSubject<Twit> = PublishSubject.create()

    private var twitsList: MutableList<Twit> = ArrayList()
        set(value) {
            field = value
            Timber.d("Twits list size = ${twitsList.size}")
            view?.dataSetChanged()
        }

    companion object {
        private const val BUFFER_TWITS = 10L
        private const val BUFFER_TIME_SECOND = 3
    }

    override fun onCreate() {
        super.onCreate()
        startStream()
    }

    private fun startStream() {
        disposables.clear()
        twitsConsumer()
        disposables += getStream()
                .subscribe({
                }, {
                    if (it is HttpException) {
                        handelError(NetworkError(it))
                    }
                })
    }

    open fun getStream(): Flowable<Twit> {
        return streamRepository.sampleStream()
                .toFlowable(BackpressureStrategy.LATEST)
                .doOnNext {
                    sentTwit(it)
                }
    }

    private fun sentTwit(twit: Twit){
        twitsSubject.onNext(twit)
    }

    private fun twitsConsumer() {
        disposables += twitsSubject
                .observeOn(Schedulers.io())
                .doOnNext {
                    it.verify()
                }
                .map<TwitOption> { TwitOption.TwitExist(it) }
                .retryWhen {
                    it.flatMap {
                        return@flatMap if (it is FieldValidationException) {
                            Timber.e(it.message)
                            Observable.just(TwitOption.TwitEmpty())
                        } else {
                            Observable.error(it)
                        }
                    }
                }
                .filter { it is TwitOption.TwitExist }
                .map { (it as TwitOption.TwitExist).twit }
                .buffer(BUFFER_TWITS, TimeUnit.SECONDS, BUFFER_TIME_SECOND)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    Timber.d("Add twits, count = ${it.size}")
                    setTwits(it)
                }
                .doOnError { Timber.e(it) }
                .map { Unit }
                .onErrorReturn { Unit }
                .subscribe()
    }

    private fun setTwits(list: MutableList<Twit>) {
        if (list.size < 1) {
            return
        }
        list.addAll(twitsList)
        twitsList = if (list.size >= BUFFER_TWITS) {
            list.subList(0, BUFFER_TWITS.toInt() - 1)
        } else {
            list.subList(0, list.size - 1)
        }
    }

    override fun refresh() {
        startStream()
    }

    override fun bindRowAtPosition(position: Int, twitView: MainContract.TwitItemView) {
        val twit = twitsList.getOrNull(position) ?: return
        twitView.setTwit(twit)
    }

    override fun getItemsCount(): Int = twitsList.size
}