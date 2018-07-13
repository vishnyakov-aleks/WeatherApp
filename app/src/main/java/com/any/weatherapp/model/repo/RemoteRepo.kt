package com.any.weatherapp.model.repo

import com.any.weatherapp.App
import com.any.weatherapp.R
import com.any.weatherapp.model.exception.WeatherLoadFailedException
import com.any.weatherapp.model.pojo.Weather
import com.any.weatherapp.model.repo.imp.IRemoteRepo
import com.any.weatherapp.model.service.OpenWeatherMapService
import com.any.weatherapp.utils.DateTimeUtils
import com.any.weatherapp.utils.DateTimeUtils.getTodaySeconds
import com.google.gson.JsonParser
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.io.IOException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt


/**
 * Created by Aleksey Vishnyakov on 12.07.2018.
 * Telegram: http://t.me/vishnyakov_aleks
 */
class RemoteRepo : IRemoteRepo {
    private val service: OpenWeatherMapService

    init {
        val client = OkHttpClient.Builder()

        client.interceptors().add(Interceptor { chain ->
            var request = chain.request()
            val url = request.url().newBuilder()
                    .addQueryParameter("APPID", "9e4aa31c9d8b9f78a54949ba0a693fd7")
                    .build()
            request = request.newBuilder().url(url).build()
            chain.proceed(request)
        })

        val retrofit = Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/")
                .client(client.build())
                .build()

        service = retrofit.create<OpenWeatherMapService>(OpenWeatherMapService::class.java)
    }

    override fun requestTownWeather(townId: Int): ArrayList<Weather> {
        val answer = try {
            service.getTownWeather(townId).execute()!!
        } catch (e: Exception) {
            when (e) {
                is UnknownHostException, is IOException -> {
                    throw WeatherLoadFailedException(townId, App.appContext.getString(R.string.connection_error))
                }

                else -> throw e
            }
        }

        val body = answer.body()?.string()
        if (answer.code() !in 200..299 || body == null || body.isNullOrEmpty()) {
            throw WeatherLoadFailedException(townId, App.appContext.getString(R.string.not_access_to_data))
        }

        val json = JsonParser().parse(body).asJsonObject

        if (json.get("cod").asInt != 200) {
            throw WeatherLoadFailedException(townId, App.appContext.getString(R.string.not_access_to_data))
        }

        val weatherMap = HashMap<Int, ArrayList<Int>>()
        json.getAsJsonArray("list").map {
            it.asJsonObject
        }.filterNot {
            it.get("dt").asInt < DateTimeUtils.getTodaySeconds()
        }.forEach {
            val diff = it.get("dt").asInt - DateTimeUtils.getTodaySeconds()
            val dayNum = TimeUnit.SECONDS.toDays(diff.toLong()).toInt()
            if (!weatherMap.containsKey(dayNum)) {
                weatherMap[dayNum] = ArrayList()
            }

            weatherMap[dayNum]!!.add(it["main"].asJsonObject["temp"].asFloat.toInt())
        }

        val listToReturn = ArrayList<Weather>()
        weatherMap.forEach { key, value ->
            val weatherDate = getTodaySeconds() + TimeUnit.DAYS.toSeconds(key.toLong()).toInt()
            val weatherAvgTemp = value.average().roundToInt()
            val updateTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()).toInt()
            listToReturn.add(Weather(townId, weatherAvgTemp, weatherDate, updateTime))
        }

        return listToReturn
    }
}