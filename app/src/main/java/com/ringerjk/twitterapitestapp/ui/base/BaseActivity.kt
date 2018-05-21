package com.ringerjk.twitterapitestapp.ui.base

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

abstract class BaseActivity: AppCompatActivity(){

	@get:LayoutRes
	abstract val layout: Int

	protected var disposables = CompositeDisposable()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(layout)
		disposables.dispose()
		disposables = CompositeDisposable()
		init()
	}

	abstract fun init()

	override fun onDestroy() {
		super.onDestroy()
		disposables.dispose()
	}


}