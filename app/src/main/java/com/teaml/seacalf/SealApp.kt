package com.teaml.seacalf

import android.app.Application

class SealApp : Application() {
    override fun onCreate() {
        super.onCreate()
        PreferencesManager.getInstance(applicationContext)
    }
}