package com.any.weatherapp.model.repo

import com.any.weatherapp.App
import com.any.weatherapp.model.pojo.Town
import com.any.weatherapp.model.repo.imp.IAssetsRepo
import java.util.*


/**
 * Created by Aleksey Vishnyakov on 12.07.2018.
 * Telegram: http://t.me/vishnyakov_aleks
 */

class AssetsRepo : IAssetsRepo {

    override fun getAllTowns(): List<Town> = readTowns("city.list.csv")

    override fun getPreActivatedTowns(): List<Town> = readTowns("first.city.list.csv")

    private fun readTowns(filename: String): List<Town> {
        val list = LinkedList<Town>()
        App.appContext.assets.open(filename)
                .bufferedReader()
                .readText().split("\n").dropLastWhile { it.trim().isEmpty() }
                .forEach {
                    val arr = it.split(",")
                    list.push(Town(arr[0].toInt(), arr[1]))
                }

        return list
    }

}