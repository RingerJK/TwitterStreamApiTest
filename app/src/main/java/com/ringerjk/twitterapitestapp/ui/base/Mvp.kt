package com.ringerjk.twitterapitestapp.ui.base

import android.support.annotation.StringRes

interface Mvp {
	interface View{
		fun showMessage(@StringRes res: Int)
	}

	interface Presenter<V: Mvp.View>{
		fun attachView(view : V)

		fun detachView()

		fun onCreate()

		fun onStart()

		fun onStop()

		fun onDestroy()
	}
}