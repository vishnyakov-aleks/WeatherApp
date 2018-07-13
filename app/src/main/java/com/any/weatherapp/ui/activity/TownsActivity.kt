package com.any.weatherapp.ui.activity

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.View
import com.any.weatherapp.R
import com.any.weatherapp.model.pojo.Town
import com.any.weatherapp.presenter.TownsActivityPresenter
import com.any.weatherapp.ui.adapter.TownsAdapter
import com.any.weatherapp.view.TownsActivityView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.chad.library.adapter.base.BaseQuickAdapter
import kotlinx.android.synthetic.main.activity_towns.*
import kotlinx.android.synthetic.main.loading_layout.*

/**
 * Created by Aleksey Vishnyakov on 13.07.2018.
 * Telegram: http://t.me/vishnyakov_aleks
 */
class TownsActivity: AbsLoadingActivity(), TownsActivityView,
        SearchView.OnQueryTextListener, BaseQuickAdapter.OnItemChildClickListener {

    @InjectPresenter
    lateinit var mPresenter: TownsActivityPresenter
    private val adapter = TownsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_towns)
        title = getString(R.string.choose_towns)
        loadingView = loading_layout
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
        adapter.onItemChildClickListener = this
        search_view.setOnQueryTextListener(this)

        if (savedInstanceState == null)
            mPresenter.loadTowns("")

    }

    override fun townListLoaded(searchPhrase: String, townList: ArrayList<Town>) {
        if (!isSearchTextEquals(searchPhrase)) return
        adapter.setNewData(townList)
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View, position: Int) {
        when (view.id) {
            R.id.checkbox -> {
                val town = this.adapter.data[position]
                mPresenter.toggleTown(town)
            }
        }
    }

    override fun townToggled(townId: Int, isActiveNow: Boolean) {
        adapter.toggleTown(townId, isActiveNow)
        setResult(Activity.RESULT_OK)
    }

    override fun showLoading(animate: Boolean) {
        if (animate) crossFadeStart() else showLoadingScreen()
    }

    override fun hideLoading() {
        crossFadeEnd()
    }

    private fun isSearchTextEquals(searchPhrase: String): Boolean {
        return search_view.query?.toString()?.trim() ?: "" == searchPhrase.trim()
    }

    override fun onQueryTextSubmit(query: String?): Boolean = true

    override fun onQueryTextChange(newText: String): Boolean {
        mPresenter.loadTowns(newText)
        return false
    }

    override fun showErrorScreen() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideErrorScreen() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}