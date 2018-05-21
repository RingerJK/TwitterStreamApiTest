package com.ringerjk.twitterapitestapp.network

import io.reactivex.Flowable
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Streaming

interface TwitterApi {

	@Streaming
    @POST("statuses/sample.json")
    fun sampleStream(): Observable<ResponseBody>


}