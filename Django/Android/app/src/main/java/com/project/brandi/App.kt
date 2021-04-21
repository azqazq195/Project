package com.project.brandi

import android.app.Application
import com.project.brandi.util.SharedPreferences

class App: Application() {
    companion object{
        lateinit var prefs: SharedPreferences
    }

    override fun onCreate() {
        prefs = SharedPreferences(applicationContext)
        super.onCreate()
    }
}