package com.kaczmarek.bggapplication.application.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kaczmarek.bggapplication.entities.BoardGameOverviewOrder
import com.kaczmarek.bggapplication.entities.database.BoardGameOverview
import com.kaczmarek.bggapplication.logic.database.AppDatabase
import kotlinx.coroutines.launch

class BoardGameOverviewListViewModel(database: AppDatabase) : BggViewModel(database) {

    private val boardGameOverviewList = MutableLiveData<List<BoardGameOverview>>()
    private var lastOrder: BoardGameOverviewOrder? = null

    fun getBoardGameOverviewList(): LiveData<List<BoardGameOverview>> = boardGameOverviewList

    fun loadBoardGameOverviewList(order: BoardGameOverviewOrder) {
        lastOrder = order
        viewModelScope.launch {
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

    fun reloadBoardGameOverviewList() {
        loadBoardGameOverviewList(lastOrder!!)
    }

    fun deleteBoardGame(boardGameOverview: BoardGameOverview) {
        viewModelScope.launch {
            database.boardGameDao().deleteBoardGameById(boardGameOverview.id)
            boardGameOverviewList.postValue(
                boardGameOverviewList.value!!.minusElement(boardGameOverview)
            )
        }
    }
}