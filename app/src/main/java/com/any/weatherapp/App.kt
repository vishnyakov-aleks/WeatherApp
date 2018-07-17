package com.any.weatherapp

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.any.weatherapp.model.repo.AssetsRepo
import com.any.weatherapp.model.repo.DatabaseRepo
import com.any.weatherapp.model.repo.RemoteRepo
import com.any.weatherapp.model.repo.imp.IAssetsRepo
import com.any.weatherapp.model.repo.imp.IDatabaseRepo
import com.any.weatherapp.model.repo.imp.IRemoteRepo
import kotlinx.coroutines.experimental.launch
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

/**
 * Created by Aleksey Vishnyakov on 12.07.2018.
 * Telegram: http://t.me/vishnyakov_aleks
 */
class App: Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var appContext: Context
            private set

        val kodein = Kodein {
            bind<IRemoteRepo>() with provider { RemoteRepo() }
            bind<IAssetsRepo>() with singleton { AssetsRepo() }
            bind<IDatabaseRepo>() with singleton { DatabaseRepo() }
        }

    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext

        launch {
            DatabaseRepo().clearOldWeather()
        }
    }
}