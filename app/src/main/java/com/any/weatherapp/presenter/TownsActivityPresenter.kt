package com.any.weatherapp.presenter

import com.any.weatherapp.App
import com.any.weatherapp.model.pojo.Town
import com.any.weatherapp.model.repo.AssetsRepo
import com.any.weatherapp.model.repo.imp.IDatabaseRepo
import com.any.weatherapp.view.TownsActivityView
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import kotlinx.coroutines.experimental.DefaultDispatcher
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
import org.kodein.di.generic.instance

/**
 * Created by Aleksey Vishnyakov on 12.07.2018.
 * Telegram: http://t.me/vishnyakov_aleks
 */

@InjectViewState
class TownsActivityPresenter: MvpPresenter<TownsActivityView>() {

    private val databaseRepo: IDatabaseRepo by App.kodein.instance()
    private var townList: ArrayList<Town>? = null
    private var assetReaderJob: Job? = null


    fun loadTowns(searchPhrase: String) {
        if (assetReaderJob != null && assetReaderJob!!.isActive) {
            assetReaderJob!!.cancel()
            assetReaderJob!!.invokeOnCompletion {
                loadTowns(searchPhrase)
            }

            return
        }

        assetReaderJob = launch (UI) {
            viewState.showLoading(townList != null)
            if (townList == null) {
                withContext(DefaultDispatcher) {
                    townList = ArrayList(AssetsRepo().getAllTowns())
                    val activeTowns = databaseRepo.getActiveTownList()
                    townList!!.filter { activeTowns.contains(it) }
                            .forEach { it.isActive = true }
                }
            }

            val list = if (searchPhrase.isNotBlank()) {
                townList!!.filter { it.name.startsWith(searchPhrase,true) } as ArrayList<Town>
            } else {
                ArrayList(townList)
            }

            viewState.townListLoaded(searchPhrase, list)
            viewState.hideLoading()
        }
    }

    fun toggleTown(town: Town) {
        launch (UI) {
            town.isActive = !town.isActive
            withContext(DefaultDispatcher) {
                if (town.isActive) {
                    databaseRepo.activateTown(town)
                } else {
                    databaseRepo.deactivateTown(town.id)
                }
            }

            viewState.townToggled(town.id, town.isActive)
        }
    }

    override fun onDestroy() {
        townList?.clear()
        townList?.trimToSize()
        townList = null
        super.onDestroy()
    }
}