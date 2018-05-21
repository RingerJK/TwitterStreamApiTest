package com.ringerjk.twitterapitestapp.model

import android.os.ParcelFormatException
import com.google.gson.annotations.SerializedName
import com.ringerjk.twitterapitestapp.model.base.FieldValidationException
import com.ringerjk.twitterapitestapp.model.base.Validator
import java.text.ParseException

data class User(
        val id: Long,
        val name: String,
        @SerializedName("profile_image_url") val profileImageUrl: String?
) : Validator {
    override fun verify() {
        if (id == null) throw FieldValidationException("id")
        if (name == null) throw FieldValidationException("name")
    }
}