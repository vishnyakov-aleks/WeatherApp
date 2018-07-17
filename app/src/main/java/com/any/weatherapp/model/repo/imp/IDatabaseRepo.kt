package com.any.weatherapp.model.repo.imp

import com.any.weatherapp.model.pojo.Town
import com.any.weatherapp.model.pojo.Weather

/**
 * Created by Aleksey Vishnyakov on 12.07.2018.
 * Telegram: http://t.me/vishnyakov_aleks
 */
interface IDatabaseRepo {

    fun getCachedWeather(townId: Int): ArrayList<Weather>

    fun addWeather(weather: Weather): Int

    fun clearOldWeather()

    fun getActiveTownList(): List<Town>

    fun activateTown(town: Town)

    fun deactivateTown(townId: Int)

    fun initializeActivatedTownsAtStart(townList: List<Town>)
}