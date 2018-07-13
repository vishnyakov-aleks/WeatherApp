package com.any.weatherapp.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.any.weatherapp.R
import com.any.weatherapp.model.exception.WeatherLoadFailedException
import com.any.weatherapp.model.pojo.Town
import com.any.weatherapp.model.pojo.Weather
import com.any.weatherapp.presenter.MainActivityPresenter
import com.any.weatherapp.ui.adapter.WeatherTownsAdapter
import com.any.weatherapp.utils.Prefs
import com.any.weatherapp.view.MainActivityView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.chad.library.adapter.base.BaseQuickAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.loading_layout.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

class MainActivity : AbsLoadingActivity(), MainActivityView, BaseQuickAdapter.OnItemChildClickListener {

    @InjectPresenter
    lateinit var mPresenter: MainActivityPresenter
    private var adapter = WeatherTownsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadingView = loading_layout
        recycler.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        recycler.adapter = adapter
        adapter.onItemChildClickListener = this

        if (savedInstanceState != null) return
        when {
            !Prefs.databaseInitialized -> mPresenter.initDatabase()

            else -> mPresenter.refreshAll(false)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.cities_settings ->
                startActivityForResult(Intent(this, TownsActivity::class.java), TOWN_SETTINGS_CHANGED_RC)
        }

        return false
    }

    override fun townWeatherUpdated(townId: Int, weatherList: ArrayList<Weather>) {
        adapter.setNewWeather(townId, weatherList)
    }

    override fun townWeatherUpdateFailed(e: WeatherLoadFailedException) {
        val town = adapter.getTownById(e.townId) ?: return
        Toast.makeText(this, "${town.name}: ${e.message}", Toast.LENGTH_SHORT).show()
    }

    override fun showRefreshLoading(townId: Int) {
        val town = adapter.getTownById(townId) ?: return
        adapter.setLoadingEnabled(town, true)
    }

    override fun hideRefreshLoading(townId: Int) {
        val town = adapter.getTownById(townId) ?: return
        adapter.setLoadingEnabled(town, false)
    }

    override fun townsLoaded(townList: List<Town>) {
        adapter.setNewData(townList)
    }

    override fun showLoading(animate: Boolean) {
        if (animate) crossFadeStart() else showLoadingScreen()
    }

    override fun hideLoading() {
        crossFadeEnd()
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        when (view.id) {
            R.id.refresh_weather_button -> launch (UI) {
                mPresenter.refreshTown(this@MainActivity.adapter.data[position].id)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TOWN_SETTINGS_CHANGED_RC && resultCode == Activity.RESULT_OK)
            mPresenter.refreshAll(true)
    }


    override fun showErrorScreen() {
        TODO("not implemented")
    }

    override fun hideErrorScreen() {
        TODO("not implemented")
    }


    companion object {
        const val TOWN_SETTINGS_CHANGED_RC = 10023
    }

}
