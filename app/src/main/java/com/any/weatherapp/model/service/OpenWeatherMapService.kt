package com.any.weatherapp.model.service

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * Created by Aleksey Vishnyakov on 12.07.2018.
 * Telegram: http://t.me/vishnyakov_aleks
 */
interface OpenWeatherMapService {

    @GET("/data/2.5/forecast")
    fun getTownWeather(@Query("id") townId: Int, @Query("units") units: String = "metric"): Call<ResponseBody>

}