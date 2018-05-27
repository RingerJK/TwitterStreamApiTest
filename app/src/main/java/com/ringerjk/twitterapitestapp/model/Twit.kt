package com.ringerjk.twitterapitestapp.model

import com.google.gson.JsonParseException
import com.google.gson.annotations.SerializedName
import com.ringerjk.twitterapitestapp.model.base.FieldValidationException
import com.ringerjk.twitterapitestapp.model.base.Validator
import java.text.ParseException

data class Twit(
	val id: Long,
	@SerializedName("created_at")
	val createdAt: String,
	val text: String,
	val user: User
) : Validator {
	override fun verify()  {
		if (id == null) throw FieldValidationException("id")
		if (createdAt == null) throw FieldValidationException("createdAt")
		if (text == null) throw FieldValidationException("text")
		if (user == null) throw FieldValidationException("user")
		user.verify()
	}
}

sealed class TwitOption() {
	data class TwitExist(val twit: Twit) : TwitOption()
	data class TwitExistFew(val twits: List<Twit>) : TwitOption()
	class TwitEmpty() : TwitOption()
}