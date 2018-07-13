package com.any.weatherapp.view

import com.any.weatherapp.model.exception.WeatherLoadFailedException
import com.any.weatherapp.model.pojo.Town
import com.any.weatherapp.model.pojo.Weather

/**
 * Created by Aleksey Vishnyakov on 12.07.2018.
 * Telegram: http://t.me/vishnyakov_aleks
 */
interface MainActivityView: LoEActivityView {

    fun townWeatherUpdated(townId: Int, weatherList: ArrayList<Weather>)

    fun townWeatherUpdateFailed(e: WeatherLoadFailedException)

    fun showRefreshLoading(townId: Int)

    fun hideRefreshLoading(townId: Int)

    fun townsLoaded(townList: List<Town>)

}