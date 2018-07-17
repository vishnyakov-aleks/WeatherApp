package com.any.weatherapp.presenter

import android.util.Log
import com.any.weatherapp.App
import com.any.weatherapp.model.exception.WeatherLoadFailedException
import com.any.weatherapp.model.repo.imp.IAssetsRepo
import com.any.weatherapp.model.repo.imp.IDatabaseRepo
import com.any.weatherapp.model.repo.imp.IRemoteRepo
import com.any.weatherapp.utils.DateTimeUtils.getSecFromMillis
import com.any.weatherapp.utils.Prefs
import com.any.weatherapp.view.MainActivityView
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import kotlinx.coroutines.experimental.DefaultDispatcher
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
import org.kodein.di.generic.instance
import java.util.concurrent.TimeUnit

/**
 * Created by Aleksey Vishnyakov on 12.07.2018.
 * Telegram: http://t.me/vishnyakov_aleks
 */

@InjectViewState
class MainActivityPresenter: MvpPresenter<MainActivityView>() {

    private val remoteRepo: IRemoteRepo by App.kodein.instance()
    private val databaseRepo: IDatabaseRepo by App.kodein.instance()
    private val assetsRepo: IAssetsRepo by App.kodein.instance()
    private val CACHED_TIME = TimeUnit.MINUTES.toSeconds(5)

    fun refreshAll(animateLoading: Boolean) {
        launch (UI) {
            val townsForUpdate = ArrayList<Int>()
            viewState.showLoading(animateLoading)
            val townList = withContext(DefaultDispatcher) { databaseRepo.getActiveTownList() }
            viewState.townsLoaded(townList)
            viewState.hideLoading()

            townList.forEach { town ->
                viewState.showRefreshLoading(town.id)
                val weatherList = databaseRepo.getCachedWeather(town.id)

                weatherList.forEach { Log.d(this@MainActivityPresenter.toString(), "${it.lastUpdate + CACHED_TIME} ${getSecFromMillis()}")}
                viewState.townWeatherUpdated(town.id, weatherList)
                if (weatherList.isNotEmpty() &&
                        weatherList.all { it.lastUpdate + CACHED_TIME > getSecFromMillis() }) {
                    viewState.hideRefreshLoading(town.id)
                    return@forEach
                }

                townsForUpdate.add(town.id)
            }

            townsForUpdate.forEach { refreshTown(it) }
        }
    }

    suspend fun refreshTown(townId: Int) {
        viewState.showRefreshLoading(townId)
        try {
            val newWeatherList = withContext(DefaultDispatcher) {
                remoteRepo.requestTownWeather(townId)
            }
            newWeatherList.forEach { databaseRepo.addWeather(it) }
            viewState.townWeatherUpdated(townId, newWeatherList)
        } catch (we: WeatherLoadFailedException) {
            viewState.townWeatherUpdateFailed(we)
        } finally {
            viewState.hideRefreshLoading(townId)
        }
    }

    fun initDatabase() {
        launch (UI) {
            viewState.showLoading(false)
            withContext(DefaultDispatcher) {
                val firstActivatedTowns =  assetsRepo.getPreActivatedTowns()
                databaseRepo.initializeActivatedTownsAtStart(firstActivatedTowns)
            }

            Prefs.databaseInitialized = true
            refreshAll(false)
        }
    }
}