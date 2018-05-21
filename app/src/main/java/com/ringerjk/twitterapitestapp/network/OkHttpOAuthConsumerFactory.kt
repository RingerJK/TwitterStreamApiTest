package com.ringerjk.twitterapitestapp.network

import android.content.Context
import com.ringerjk.twitterapitestapp.R
import com.twitter.sdk.android.core.TwitterSession
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer

class OkHttpOAuthConsumerFactory(context: Context, private val twitterSession: TwitterSession) {

    private val consumer: OkHttpOAuthConsumer = OkHttpOAuthConsumer(
            context.getString(R.string.twitter_api_consume_key),
            context.getString(R.string.twitter_api_consume_secret))

    fun getConsumer(): OkHttpOAuthConsumer =
        consumer.apply {
            setTokenWithSecret(twitterSession.authToken.token, twitterSession.authToken.secret)
        }
}