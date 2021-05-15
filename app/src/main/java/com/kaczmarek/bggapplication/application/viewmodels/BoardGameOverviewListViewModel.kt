package com.kaczmarek.bggapplication.application.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaczmarek.bggapplication.entities.BoardGameOverviewOrder
import com.kaczmarek.bggapplication.entities.database.BoardGameOverview
import com.kaczmarek.bggapplication.logic.database.AppDatabase
import kotlinx.coroutines.launch

class BoardGameOverviewListViewModel(database: AppDatabase) : BggViewModel(database) {

    private val boardGameOverviewList = MutableLiveData<List<BoardGameOverview>>()
    private var lastOrder: BoardGameOverviewOrder? = null

    fun getBoardGameOverviewList(order: BoardGameOverviewOrder):
            LiveData<List<BoardGameOverview>> {

        if (lastOrder != order) {
            loadBoardGameOverviewList(order)
        }
        return boardGameOverviewList
    }

    fun deleteBoardGame(id: Long) {
        viewModelScope.launch {
            database.boardGameDao().deleteBoardGameById(id)
            loadBoardGameOverviewList(lastOrder!!)
        }
    }

    private fun loadBoardGameOverviewList(order: BoardGameOverviewOrder) {
        lastOrder = order
        viewModelScope.launch {
            boardGameOverviewList.postValue(listOf())

            val dao = database.boardGameDao()
            val list = when (order) {
                BoardGameOverviewOrder.RANK_ASC -> dao.getBoardGameOverviewsRankAsc()
                BoardGameOverviewOrder.RANK_DESC -> dao.getBoardGameOverviewsRankDesc()
                BoardGameOverviewOrder.YEAR_ASC -> dao.getBoardGameOverviewsYearAsc()
                BoardGameOverviewOrder.YEAR_DESC -> dao.getBoardGameOverviewsYearDesc()
            }

            boardGameOverviewList.postValue(list)
        }
    }
}