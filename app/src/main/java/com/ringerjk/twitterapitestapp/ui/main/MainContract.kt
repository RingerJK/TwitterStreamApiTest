package com.ringerjk.twitterapitestapp.ui.main

import com.ringerjk.twitterapitestapp.model.Twit
import com.ringerjk.twitterapitestapp.ui.base.Mvp

interface MainContract {
	interface View : Mvp.View {
		fun dataSetChanged()

		fun showProgress()

		fun hideProgress()
	}

	interface Presenter : Mvp.Presenter<MainContract.View>{

		fun bindRowAtPosition(position: Int, twitView: TwitItemView)

		fun getItemsCount(): Int

		fun refresh()
	}

	interface TwitItemView {
		fun setTwit(twit: Twit)
	}
}