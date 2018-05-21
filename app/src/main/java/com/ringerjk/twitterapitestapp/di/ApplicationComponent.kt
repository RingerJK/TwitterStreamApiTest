package com.ringerjk.twitterapitestapp.di

import android.content.Context
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import com.ringerjk.twitterapitestapp.BuildConfig
import com.ringerjk.twitterapitestapp.network.OkHttpOAuthConsumerFactory
import com.ringerjk.twitterapitestapp.network.RetrofitFactory
import com.ringerjk.twitterapitestapp.network.TwitterApi
import com.ringerjk.twitterapitestapp.repository.StreamRepository
import com.ringerjk.twitterapitestapp.ui.main.MainActivityComponent
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.TwitterSession
import dagger.Component
import dagger.Module
import dagger.Provides
import se.akerfeldt.okhttp.signpost.SigningInterceptor
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Component(modules = [(ApplicationModule::class), (NetworkModule::class)])
interface ApplicationComponent {

//    fun twitterApi(): TwitterApi

	fun streamRepository(): StreamRepository

	@Named(APP_CONTEXT)
	fun appContext(): Context

	fun twitterSession(): TwitterSession

	fun mainActivityComponent(): MainActivityComponent
}

const val APP_CONTEXT = "APP_CONTEXT"

@Module
class ApplicationModule(private val context: Context) {
	@Provides
	@Singleton
	@Named(APP_CONTEXT)
	fun provideAppContext(): Context = context

	@Provides
	@Singleton
	fun gson(): Gson = Gson()
}

const val BASE_URL = "BASE_URL"

@Module
class NetworkModule {

	@get:Provides
	@get:Singleton
	@get:Named(BASE_URL)
	val baseUrl
		get() = BuildConfig.URL_ENDPOINT

	@Singleton
	@Provides
	fun provideTwitterSession(): TwitterSession =
			TwitterCore.getInstance().sessionManager.activeSession

	@Singleton
	@Provides
	fun provideSigninInterceptor(@Named(APP_CONTEXT) context: Context, twitterSession: TwitterSession): SigningInterceptor =
			SigningInterceptor(OkHttpOAuthConsumerFactory(context, twitterSession).getConsumer())

	@Singleton
	@Provides
	fun provideStathoInterceptor(): StethoInterceptor = StethoInterceptor()

	@Singleton
	@Provides
	fun provideRetrofitFactory(@Named(BASE_URL) baseUrl: String,
	                           signingInterceptor: SigningInterceptor,
	                           stethoInterceptor: StethoInterceptor): RetrofitFactory =
			RetrofitFactory(baseUrl, signingInterceptor, stethoInterceptor)

	@Singleton
	@Provides
	fun provideTwitterClient(retrofitFactory: RetrofitFactory): TwitterApi =
			retrofitFactory.retrofit.create(TwitterApi::class.java)

	@Singleton
	@Provides
	fun provideStreamRepository(twitterApi: TwitterApi, gson: Gson): StreamRepository = StreamRepository(twitterApi, gson)
}