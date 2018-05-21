package com.ringerjk.twitterapitestapp.ui.main

import com.google.gson.Gson
import com.ringerjk.twitterapitestapp.model.Twit
import com.ringerjk.twitterapitestapp.model.User
import com.ringerjk.twitterapitestapp.network.TwitterApi
import com.ringerjk.twitterapitestapp.repository.StreamRepository
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import junit.framework.Assert
import junit.framework.TestCase
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.inOrder
import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit


@RunWith(MockitoJUnitRunner::class)
class MainPresenterTest : TestCase() {

    lateinit var mainPresenter: MainPresenter
    @Mock
    lateinit var streamRepository: StreamRepository
    @Mock
    lateinit var mainView: MainContract.View

    companion object {
        @BeforeClass
        @JvmStatic
        fun setUpRxSchedulers() {
            val immediate = object : Scheduler() {
                override fun scheduleDirect(run: Runnable, delay: Long, unit: TimeUnit): Disposable {
                    return super.scheduleDirect(run, 0, unit)
                }

                override fun createWorker(): Scheduler.Worker {
                    return ExecutorScheduler.ExecutorWorker(Executor { it.run() })
                }
            }

            RxJavaPlugins.setInitIoSchedulerHandler { scheduler -> immediate }
            RxJavaPlugins.setInitComputationSchedulerHandler { scheduler -> immediate }
            RxJavaPlugins.setInitNewThreadSchedulerHandler { scheduler -> immediate }
            RxJavaPlugins.setInitSingleSchedulerHandler { scheduler -> immediate }
            RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler -> immediate }
        }

    }

    @Before
    fun before() {
        mainPresenter = MainPresenter(streamRepository)
	    mainPresenter.attachView(mainView)
    }

    @After
    fun after() {
    }

    @Test
    fun testStream() {
	    val twit1 = Twit(0, "", "text1", User(0, "Name1", "Url1"))
	    `when`(streamRepository.sampleStream()).thenReturn(Observable.just(twit1))

        val testObs = mainPresenter.getStream()
            .test()

	    testObs.awaitTerminalEvent(5, TimeUnit.SECONDS)

	    testObs.
		    assertNoErrors()
		    .assertValue { it == twit1 }

	    testObs.assertTerminated()
    }
}