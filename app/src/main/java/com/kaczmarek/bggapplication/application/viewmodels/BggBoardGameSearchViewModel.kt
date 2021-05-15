package com.kaczmarek.bggapplication.application.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kaczmarek.bggapplication.R
import com.kaczmarek.bggapplication.entities.bggapi.BggApiResponse
import com.kaczmarek.bggapplication.entities.bggapi.BggBoardGameOverview
import com.kaczmarek.bggapplication.logic.bggapi.BggApiDao
import com.kaczmarek.bggapplication.logic.database.AppDatabase
import kotlinx.coroutines.launch

class BggBoardGameSearchViewModel(database: AppDatabase) : BggViewModel(database) {

    private val overviewList = MutableLiveData<List<BggBoardGameOverview>>()

    fun getOverviewList(): LiveData<List<BggBoardGameOverview>> = overviewList

    fun searchByName(name: String) {
        viewModelScope.launch {
            setIsLoading(true)
            try {
                overviewList.postValue(loadOverviewsByName(name))
            } catch (e: Exception) {
                setErrorMessage(R.string.err_bgg_api_request_failed)
            }
            setIsLoading(false)
        }
    }

    private suspend fun loadOverviewsByName(name: String): List<BggBoardGameOverview> {
        val response = BggApiDao().getGameOverviewsByName(name)
        if (response is BggApiResponse.Success) {
            return response.data
        }
        throw RuntimeException()
    }
}