package com.ringerjk.twitterapitestapp.ui.base

import android.support.annotation.CallSuper
import com.ringerjk.twitterapitestapp.network.NetworkError
import io.reactivex.disposables.CompositeDisposable
import java.lang.ref.WeakReference

abstract class BasePresenter<T : Mvp.View> : Mvp.Presenter<T> {

	private var viewWeek: WeakReference<T>? = null

	var disposables = CompositeDisposable()

	val view: T?
		get() = viewWeek?.get()

	val isViewAttach: Boolean
		get() = view != null

	override fun attachView(view: T) {
		viewWeek = WeakReference(view)
	}

	override fun detachView() {
		viewWeek?.clear()
	}

	@CallSuper
	override fun onCreate() {
		disposables.dispose()
		disposables = CompositeDisposable()
	}

	override fun onStart() {
	}

	override fun onStop() {
	}

	@CallSuper
	override fun onDestroy() {
		disposables.dispose()
	}

	protected fun handelError(networkError: NetworkError){
		view?.showMessage(networkError.getMessage())
	}
}