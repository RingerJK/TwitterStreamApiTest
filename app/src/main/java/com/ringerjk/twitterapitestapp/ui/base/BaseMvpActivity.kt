package com.ringerjk.twitterapitestapp.ui.base

import android.os.Bundle
import android.widget.Toast
import com.ringerjk.twitterapitestapp.app.App
import com.ringerjk.twitterapitestapp.di.ApplicationComponent

abstract class BaseMvpActivity<V : Mvp.View, P : Mvp.Presenter<V>> : BaseActivity(), Mvp.View {

	abstract var presenter: P

	abstract fun inject(appComponent: ApplicationComponent)

	override fun onCreate(savedInstanceState: Bundle?) {
		inject((application as App).appComponent)
		super.onCreate(savedInstanceState)
		presenter.attachView(this as V)
		presenter.onCreate()
	}

	override fun onStart() {
		super.onStart()
		presenter.onStart()
	}

	override fun onStop() {
		presenter.onStop()
		super.onStop()
	}

	override fun onDestroy() {
		presenter.onDestroy()
		presenter.detachView()
		super.onDestroy()
	}

	override fun showMessage(res: Int){
		Toast.makeText(this, res, Toast.LENGTH_SHORT).show()
	}
}