package com.kaczmarek.bggapplication.application.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kaczmarek.bggapplication.entities.database.BoardGameOverview
import com.kaczmarek.bggapplication.entities.database.Rank
import com.kaczmarek.bggapplication.logic.database.AppDatabase
import kotlinx.coroutines.launch

class BoardGameRankingViewModel(database: AppDatabase) : BggViewModel(database) {

    private val boardGameOverview = MutableLiveData<BoardGameOverview>()
    private val boardGameRanking = MutableLiveData<List<Rank>>()

    fun getBoardGameOverview(): LiveData<BoardGameOverview> = boardGameOverview
    fun getBoardGameRanking(): LiveData<List<Rank>> = boardGameRanking

    fun loadTarget(id: Long) {
        viewModelScope.launch {
            boardGameOverview.postValue(loadOverview(id))
            boardGameRanking.postValue(loadRanking(id))
        }
    }

    private suspend fun loadOverview(id: Long): BoardGameOverview {
        return database.boardGameDao().getOverviewById(id)
    }

    private suspend fun loadRanking(id: Long): List<Rank> {
        return database.rankDao().getBoardGameRanks(id)
    }
}