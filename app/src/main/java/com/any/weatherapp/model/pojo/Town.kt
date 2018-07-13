package com.any.weatherapp.model.pojo

/**
 * Created by Aleksey Vishnyakov on 12.07.2018.
 * Telegram: http://t.me/vishnyakov_aleks
 */
data class Town(val id: Int, val name: String) {
    var weatherList = ArrayList<Weather>(7)
    var isLoading = false
    var isActive = false


}