package com.any.weatherapp

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.any.weatherapp.model.repo.DatabaseRepo
import kotlinx.coroutines.experimental.launch

/**
 * Created by Aleksey Vishnyakov on 12.07.2018.
 * Telegram: http://t.me/vishnyakov_aleks
 */
class App: Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext

        launch {
            DatabaseRepo().clearOldWeather()
        }
    }
}