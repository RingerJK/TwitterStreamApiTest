package com.ringerjk.twitterapitestapp.network

import android.support.annotation.StringRes
import com.ringerjk.twitterapitestapp.R
import retrofit2.HttpException

class NetworkError(private val exception: HttpException) : Throwable(exception) {

	@StringRes
	fun getMessage(): Int =
		when (exception.code()) {
			401 -> R.string.error_unauthorized
			404 -> R.string.error_page_not_found
			408 -> R.string.error_timeout
			else -> R.string.error_internal
		}
}