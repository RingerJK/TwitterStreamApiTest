package com.ringerjk.twitterapitestapp.app

import android.app.Application
import android.util.Log
import com.facebook.stetho.Stetho
import com.ringerjk.twitterapitestapp.R
import com.ringerjk.twitterapitestapp.di.ApplicationComponent
import com.ringerjk.twitterapitestapp.di.ApplicationModule
import com.ringerjk.twitterapitestapp.di.DaggerApplicationComponent
import com.ringerjk.twitterapitestapp.di.NetworkModule
import com.twitter.sdk.android.core.DefaultLogger
import com.twitter.sdk.android.core.Twitter
import com.twitter.sdk.android.core.TwitterAuthConfig
import com.twitter.sdk.android.core.TwitterConfig
import timber.log.Timber
import timber.log.Timber.DebugTree


class App : Application() {

    lateinit var appComponent: ApplicationComponent
        private set

    companion object {
        lateinit var application: App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        initTimber()
        setupAppComponent()
        setupTwitter()

	    Stetho.initializeWithDefaults(this)
    }

    private fun initTimber() = Timber.plant(DebugTree())

    private fun setupAppComponent() {
        appComponent = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .networkModule(NetworkModule())
                .build()
    }

    private fun setupTwitter() {
        val config = TwitterConfig.Builder(this)
                .logger(DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(TwitterAuthConfig(getString(R.string.twitter_api_consume_key), getString(R.string.twitter_api_consume_secret)))
                .debug(true)
                .build()
        Twitter.initialize(config)
    }
}