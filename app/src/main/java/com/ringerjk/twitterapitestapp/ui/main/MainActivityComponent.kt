package com.ringerjk.twitterapitestapp.ui.main

import dagger.Module
import dagger.Subcomponent
import javax.inject.Scope

@Scope
@Retention
annotation class MainActivityScope

@Subcomponent
interface MainActivityComponent {

	@MainActivityScope
	fun inject(activity: MainActivity)
}
