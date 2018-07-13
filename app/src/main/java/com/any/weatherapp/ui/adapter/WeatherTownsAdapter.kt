package com.any.weatherapp.ui.adapter

import android.annotation.SuppressLint
import android.widget.TextView
import com.any.weatherapp.R
import com.any.weatherapp.model.pojo.Town
import com.any.weatherapp.model.pojo.Weather
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.weather_town_cardview.view.*
import org.joda.time.LocalDateTime
import java.util.*

/**
 * Created by Aleksey Vishnyakov on 12.07.2018.
 * Telegram: http://t.me/vishnyakov_aleks
 */
class WeatherTownsAdapter : BaseQuickAdapter<Town, BaseViewHolder>(R.layout.weather_town_cardview) {
    @SuppressLint("SetTextI18n")
    override fun convert(helper: BaseViewHolder, item: Town) {
        helper.setText(R.id.town_textview, item.name)

        helper.itemView.weather_lines_layout.removeAllViews()
        item.weatherList.forEach { weather ->
            helper.itemView.weather_lines_layout
                    .addView(TextView(mContext).apply {
                        val day = LocalDateTime.fromDateFields(Date(weather.date * 1000L)).dayOfMonth
                        text = "Day $day: ${weather.cValue}"
                    })
        }

        helper.setVisible(R.id.refresh_weather_button, !item.isLoading)
        helper.setVisible(R.id.refresh_weather_progress, item.isLoading)

        helper.addOnClickListener(R.id.refresh_weather_button)
    }

    fun getTownById(townId: Int): Town? = data.firstOrNull { it.id == townId }

    private fun getTownPositionById(townId: Int): Int? = data.indexOf(getTownById(townId)) + headerLayoutCount

    fun setLoadingEnabled(town: Town, b: Boolean) {
        town.isLoading = b
        notifyItemChanged(getTownPositionById(town.id)?: return)
    }

    fun setNewWeather(townId: Int, weatherList: ArrayList<Weather>) {
        getTownById(townId)?.weatherList = weatherList
        notifyItemChanged(getTownPositionById(townId)?: return)
    }
}