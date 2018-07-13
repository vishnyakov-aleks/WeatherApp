package com.any.weatherapp.utils

import org.joda.time.LocalDateTime
import java.util.concurrent.TimeUnit

/**
 * Created by Aleksey Vishnyakov on 12.07.2018.
 * Telegram: http://t.me/vishnyakov_aleks
 */

object DateTimeUtils {

    fun getTodaySeconds(): Int {
        val todayMillis = LocalDateTime.now()
                .withHourOfDay(0)
                .withMinuteOfHour(0)
                .withSecondOfMinute(0)
                .withMillisOfSecond(0)
                .toDate().time

        return getSecFromMillis(todayMillis)
    }

    fun getSecFromMillis(millis: Long = System.currentTimeMillis()): Int = TimeUnit.MILLISECONDS.toSeconds(millis).toInt()
}