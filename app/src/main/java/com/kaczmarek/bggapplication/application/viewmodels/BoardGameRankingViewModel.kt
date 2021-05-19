package com.kaczmarek.bggapplication.application.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kaczmarek.bggapplication.entities.database.Rank
import com.kaczmarek.bggapplication.logic.database.AppDatabase
import kotlinx.coroutines.launch

class BoardGameRankingViewModel(database: AppDatabase) : BggViewModel(database) {

    private val boardGameRanking = MutableLiveData<List<Rank>>()

    fun getBoardGameRanking(): LiveData<List<Rank>> = boardGameRanking

    fun loadTarget(bggId: Long) {
        viewModelScope.launch {
            boardGameRanking.postValue(loadRanking(bggId))
        }
    }

    private suspend fun loadRanking(bggId: Long): List<Rank> {
        return database.rankDao().getBoardGameRanks(bggId)
    }
}