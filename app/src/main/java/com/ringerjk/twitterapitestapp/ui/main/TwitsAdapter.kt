package com.ringerjk.twitterapitestapp.ui.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.ringerjk.twitterapitestapp.R
import com.ringerjk.twitterapitestapp.model.Twit
import kotlinx.android.synthetic.main.twit_item.view.*
import timber.log.Timber

class TwitsAdapter(private val presenter: MainContract.Presenter) : RecyclerView.Adapter<TwitsAdapter.TwitterViewHolderTwit>() {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TwitterViewHolderTwit {
		val view = LayoutInflater.from(parent.context).inflate(R.layout.twit_item, parent, false)
		return TwitterViewHolderTwit(view)
	}

	override fun getItemCount(): Int = presenter.getItemsCount()

	override fun onBindViewHolder(holder: TwitterViewHolderTwit, position: Int) {
		presenter.bindRowAtPosition(position, holder)
	}

	class TwitterViewHolderTwit(itemView: View) : RecyclerView.ViewHolder(itemView), MainContract.TwitItemView {
		private val twitText = itemView.twit_text
		private val userImage = itemView.user_image
		private val userName = itemView.user_name

		override fun setTwit(twit: Twit) {
			twitText.text = twit.text
			Glide.with(itemView).load(twit.user.profileImageUrl).into(userImage)
			userName.text = twit.user.name
			Timber.d("Twit text = ${twit.text}")
		}
	}
}