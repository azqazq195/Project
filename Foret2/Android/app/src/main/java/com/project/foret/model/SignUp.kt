package com.project.foret.model

import android.text.TextWatcher

data class SignUp(
    val index: Int,
    val hintMessage: String,
    val errorMessage: String,
    var value: String?,
    var check: Boolean,
)
