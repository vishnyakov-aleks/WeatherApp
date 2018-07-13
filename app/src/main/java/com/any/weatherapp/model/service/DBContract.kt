package com.any.weatherapp.model.service

import android.provider.BaseColumns

/**
 * Created by Aleksey Vishnyakov on 12.07.2018.
 * Telegram: http://t.me/vishnyakov_aleks
 */
object DBContract {

    object WeatherEntry : BaseColumns {
        const val WEATHER_TABLE_NAME = "weather"

        enum class COLUMNS {
            DATE,
            OWNER_TOWN,
            VALUE_C,
            UPDATED_TIME
        }
    }

    object TownEntry : BaseColumns {
        const val TOWN_TABLE_NAME = "towns"

        enum class COLUMNS {
            TOWN_ID,
            TOWN_NAME
        }
    }
}