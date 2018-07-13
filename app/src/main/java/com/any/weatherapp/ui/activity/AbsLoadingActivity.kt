package com.any.weatherapp.ui.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import com.arellomobile.mvp.MvpAppCompatActivity

/**
 * Created by Aleksey Vishnyakov on 12.07.2018.
 * Telegram: http://t.me/vishnyakov_aleks
 */

abstract class AbsLoadingActivity: MvpAppCompatActivity() {
    lateinit var loadingView: View
    var isLoadingShow: Boolean = false
        private set

    fun crossFadeStart() {
        isLoadingShow = true
        if (loadingView.visibility != View.VISIBLE) {
            loadingView.alpha = 0f
            loadingView.visibility = View.VISIBLE
            loadingView.animate()?.alpha(1.0f)?.setDuration(280)?.setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    loadingView.alpha = 1f

                }
            })
        }


    }

    fun crossFadeEnd() {
        isLoadingShow = false
        if (loadingView.visibility == View.VISIBLE) {
            loadingView.animate()?.alpha(0f)?.setDuration(280)?.setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    loadingView.visibility = View.GONE
                    loadingView.alpha = 0f

                }
            })
        }

    }

    fun showLoadingScreen() {
        if (!isLoadingShow) {
            isLoadingShow = true
            loadingView.visibility = View.VISIBLE
            loadingView.alpha = 1f
        }
    }

}