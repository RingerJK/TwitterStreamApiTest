package com.ringerjk.twitterapitestapp.ui.login

import android.content.Intent
import com.ringerjk.twitterapitestapp.R
import com.ringerjk.twitterapitestapp.app.App
import com.ringerjk.twitterapitestapp.ui.base.BaseActivity
import com.ringerjk.twitterapitestapp.ui.main.MainActivity
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.TwitterSession
import kotlinx.android.synthetic.main.login_activity.*
import timber.log.Timber

class LoginActivity : BaseActivity() {

	override val layout: Int = R.layout.login_activity

	override fun init() {

		if((application as App).appComponent.twitterSession() != null){
			startActivity(MainActivity.intent(this))
			return
		}

		twitter_login_btn.callback = object : Callback<TwitterSession>() {
			override fun success(result: Result<TwitterSession>?) {
				result?.let {
					startActivity(MainActivity.intent(this@LoginActivity))
				}
			}

			override fun failure(exception: TwitterException?) {
				Timber.e(exception, exception?.message)
			}
		}
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		twitter_login_btn.onActivityResult(requestCode, resultCode, data)
	}
}