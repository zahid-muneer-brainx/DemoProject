package com.example.demoproject

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {

    companion object {
        lateinit var appInstance: MyApplication
        fun getAppContext(): Context = appInstance.applicationContext
    }

    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }

}