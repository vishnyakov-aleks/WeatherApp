package com.any.weatherapp.view

import com.any.weatherapp.model.pojo.Town

/**
 * Created by Aleksey Vishnyakov on 12.07.2018.
 * Telegram: http://t.me/vishnyakov_aleks
 */
interface TownsActivityView: LoEActivityView {

    fun townListLoaded(searchPhrase: String, townList: ArrayList<Town>)

    fun townToggled(townId: Int, isActiveNow: Boolean)

}