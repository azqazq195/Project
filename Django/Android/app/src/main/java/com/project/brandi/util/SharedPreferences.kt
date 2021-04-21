package com.project.brandi.util

import android.content.Context

class SharedPreferences(context: Context) {
    val PREFS_FLIENAME = "Brandi"
    val prefs = context.getSharedPreferences(PREFS_FLIENAME, Context.MODE_PRIVATE)

    fun putString(key: String, value: String){
        prefs.edit().putString(key, value).apply()
    }

    fun getString(key: String) : String? {
        return prefs.getString(key, "")
    }

    fun removeAll() {
        prefs.edit().apply {
            remove("token")
            remove("name")
            remove("email")
        }.apply()
    }
}