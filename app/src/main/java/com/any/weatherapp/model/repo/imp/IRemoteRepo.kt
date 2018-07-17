package com.any.weatherapp.model.repo.imp

import com.any.weatherapp.model.pojo.Weather

/**
 * Created by Aleksey Vishnyakov on 12.07.2018.
 * Telegram: http://t.me/vishnyakov_aleks
 */
interface IRemoteRepo {

    fun requestTownWeather(townId: Int): ArrayList<Weather>

}