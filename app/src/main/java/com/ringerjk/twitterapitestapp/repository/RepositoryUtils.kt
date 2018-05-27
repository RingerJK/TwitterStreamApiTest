package com.ringerjk.twitterapitestapp.repository

import com.google.gson.Gson
import io.reactivex.Observable
import okio.BufferedSource
import timber.log.Timber
import java.io.IOException
import java.io.UncheckedIOException

class RepositoryUtils(private val gson: Gson) {
	fun <T> eventsConverter(source: BufferedSource, clazz: Class<T>): Observable<T> {
		return Observable.create<T> {
			var sourceCompleted = false
			try {
				if (!it.isDisposed) {
					while (!source.exhausted()) {
						val str = source.readUtf8Line()
						val obj = gson.fromJson<T>(str, clazz)
						Timber.d(obj.toString())
						it.onNext(obj)
					}
					it.onComplete()
				}
			} catch (e: IOException) {
				if (e.message == "Socket closed") {
					Timber.e(e.message)
					sourceCompleted = true
					it.onComplete()
				} else {
					Timber.e(e)
					it.onError(e)
				}
			}
		}
	}
}