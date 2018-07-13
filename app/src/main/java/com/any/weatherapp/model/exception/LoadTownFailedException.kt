package com.any.weatherapp.model.exception

import com.any.weatherapp.model.pojo.Weather

/**
 * Created by Aleksey Vishnyakov on 12.07.2018.
 * Telegram: http://t.me/vishnyakov_aleks
 */
class WeatherLoadFailedException(val townId: Int, message: String): Exception(message)