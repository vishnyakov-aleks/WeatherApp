package com.any.weatherapp.view

import com.arellomobile.mvp.MvpView

/**
 * Created by Aleksey Vishnyakov on 12.07.2018.
 * Telegram: http://t.me/vishnyakov_aleks
 */
interface LoEActivityView: MvpView {

    fun showLoading(animate: Boolean = true)

    fun hideLoading()

    fun showErrorScreen()

    fun hideErrorScreen()

}