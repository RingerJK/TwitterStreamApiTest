package com.ringerjk.twitterapitestapp.repository

import com.google.gson.Gson
import com.ringerjk.twitterapitestapp.model.Twit
import com.ringerjk.twitterapitestapp.network.TwitterApi
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

open class StreamRepository(private val twitterApi: TwitterApi, gson: Gson) {

    private val reposiorytUtils = RepositoryUtils(gson)

    open fun sampleStream(): Observable<Twit> =
            twitterApi.sampleStream()
                    .flatMap { events -> reposiorytUtils.eventsConverter(events.source(), Twit::class.java) }
                    .subscribeOn(Schedulers.io())
}