package com.any.weatherapp.utils

import android.content.Context.MODE_PRIVATE
import com.any.weatherapp.App

/**
 * Created by Aleksey Vishnyakov on 13.07.2018.
 * Telegram: http://t.me/vishnyakov_aleks
 */
object Prefs {
    private const val DATABASE_INITIALIZED = "DATABASE_INITIALIZED"
    private val sp = App.appContext.getSharedPreferences(this.toString(), MODE_PRIVATE)


    var databaseInitialized: Boolean
        get() = sp.getBoolean(DATABASE_INITIALIZED, false)
        set(value) = sp.edit().putBoolean(DATABASE_INITIALIZED, value).apply()
}