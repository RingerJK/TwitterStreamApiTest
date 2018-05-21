package com.ringerjk.twitterapitestapp.ui.main

import android.content.Context
import android.content.Intent
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import com.ringerjk.twitterapitestapp.R
import com.ringerjk.twitterapitestapp.di.ApplicationComponent
import com.ringerjk.twitterapitestapp.ui.base.BaseMvpActivity
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import javax.inject.Inject


class MainActivity : BaseMvpActivity<MainContract.View, MainPresenter>(), MainContract.View, SwipeRefreshLayout.OnRefreshListener {

	@Inject
	override lateinit var presenter: MainPresenter

	private lateinit var adapter: TwitsAdapter

	override val layout: Int = R.layout.activity_main

	companion object {
		fun intent(context: Context): Intent = Intent(context, MainActivity::class.java)
	}

	override fun inject(appComponent: ApplicationComponent) = appComponent.mainActivityComponent().inject(this)

	override fun init() {
		adapter = TwitsAdapter(presenter)
		twits_list.layoutManager = LinearLayoutManager(this)
		twits_list.adapter = adapter

		swipe_refresh_view.setOnRefreshListener(this)
	}

	override fun onRefresh() {
		presenter.refresh()
	}

	override fun showProgress() {

	}

	override fun hideProgress() {
		swipe_refresh_view.isRefreshing = false
	}

	override fun dataSetChanged() {
		Timber.d("dataSetChanged")
		adapter.notifyDataSetChanged()
	}
}
