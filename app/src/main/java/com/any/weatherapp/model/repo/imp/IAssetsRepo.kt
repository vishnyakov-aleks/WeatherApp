package com.any.weatherapp.model.repo.imp

import com.any.weatherapp.model.pojo.Town

/**
 * Created by Aleksey Vishnyakov on 12.07.2018.
 * Telegram: http://t.me/vishnyakov_aleks
 */
interface IAssetsRepo {

    fun getAllTowns(): List<Town>

    fun getPreActivatedTowns(): List<Town>

}