package com.any.weatherapp.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Typeface
import com.any.weatherapp.R
import com.any.weatherapp.model.pojo.Town
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by Aleksey Vishnyakov on 12.07.2018.
 * Telegram: http://t.me/vishnyakov_aleks
 */
class TownsAdapter : BaseQuickAdapter<Town, BaseViewHolder>(R.layout.town_row) {
    @SuppressLint("SetTextI18n")
    override fun convert(helper: BaseViewHolder, item: Town) {
        helper.setText(R.id.checkbox, item.name)
        helper.setChecked(R.id.checkbox, item.isActive)
        helper.setTypeface(R.id.checkbox, if (item.isActive) Typeface.DEFAULT_BOLD else Typeface.DEFAULT)
        helper.addOnClickListener(R.id.checkbox)
    }

    override fun setNewData(data: MutableList<Town>?) = super.setNewData(data?.sortedByDescending { it.isActive })

    private fun getTownById(townId: Int): Town? = data.firstOrNull { it.id == townId }

    private fun getTownPositionById(townId: Int): Int? = data.indexOf(getTownById(townId)) + headerLayoutCount

    fun toggleTown(townId: Int, b: Boolean) {
        getTownById(townId)?.isActive = b
        notifyItemChanged(getTownPositionById(townId)?: return)
    }
}