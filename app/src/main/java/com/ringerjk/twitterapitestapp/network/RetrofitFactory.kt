package com.ringerjk.twitterapitestapp.network

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.ringerjk.twitterapitestapp.di.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import se.akerfeldt.okhttp.signpost.SigningInterceptor
import javax.inject.Named

class RetrofitFactory(@Named(BASE_URL) private val baseUrl: String,
                      private val signingInterceptor: SigningInterceptor,
                      private val stethoInterceptor: StethoInterceptor) {
    val retrofit: Retrofit

    init {
        retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(getConverterFactory())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(getClient())
                .build()
    }

    private fun getConverterFactory(): Converter.Factory = GsonConverterFactory.create()

    private fun getClient(): OkHttpClient =
            OkHttpClient.Builder()
                    .addInterceptor(signingInterceptor)
                    .addNetworkInterceptor(stethoInterceptor)
                    .build()
}