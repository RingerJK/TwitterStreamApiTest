package com.ringerjk.twitterapitestapp.model.base

interface Validator {
    fun verify()
}

class FieldValidationException(fieldName: String): Exception("Field Name = $fieldName"){
}