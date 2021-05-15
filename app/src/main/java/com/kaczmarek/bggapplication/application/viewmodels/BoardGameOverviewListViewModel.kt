package com.kaczmarek.bggapplication.application.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaczmarek.bggapplication.entities.BoardGameOverviewOrder
import com.kaczmarek.bggapplication.entities.database.BoardGameOverview
import com.kaczmarek.bggapplication.logic.database.AppDatabase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BoardGameOverviewListViewModel(private val database: AppDatabase) : ViewModel() {

    private val isLoading = MutableLiveData<Boolean>().apply { setValue(false) }
    private val boardGameOverviewList = MutableLiveData<List<BoardGameOverview>>()
    private var lastOrder: BoardGameOverviewOrder? = null

    fun getIsLoading(): LiveData<Boolean> = isLoading

    fun getBoardGameOverviewList(order: BoardGameOverviewOrder):
            LiveData<List<BoardGameOverview>> {

        if (lastOrder != order) {
            loadBoardGameOverviewList(order)
        }
        return boardGameOverviewList
    }

    private fun loadBoardGameOverviewList(order: BoardGameOverviewOrder) {
        lastOrder = order
        viewModelScope.launch {
            boardGameOverviewList.postValue(listOf())
            isLoading.postValue(true)
            /*
            val dao = database.boardGameDao()
            val list = when (order) {
                BoardGameOverviewOrder.RANK_ASC -> dao.getBoardGameOverviewsRankAsc()
                BoardGameOverviewOrder.RANK_DESC -> dao.getBoardGameOverviewsRankDesc()
                BoardGameOverviewOrder.YEAR_ASC -> dao.getBoardGameOverviewsYearAsc()
                BoardGameOverviewOrder.YEAR_DESC -> dao.getBoardGameOverviewsYearDesc()
            }

            boardGameOverviewList.postValue(list)*/
            delay(500)
            boardGameOverviewList.postValue(listOf())
            isLoading.postValue(false)
        }
    }
}