package com.kaczmarek.bggapplication.application.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kaczmarek.bggapplication.entities.database.BoardGame
import com.kaczmarek.bggapplication.entities.database.BoardGameLocationRelation
import com.kaczmarek.bggapplication.entities.database.NewestRankView
import com.kaczmarek.bggapplication.entities.database.Rank
import com.kaczmarek.bggapplication.logic.database.AppDatabase
import kotlinx.coroutines.launch

class BoardGameDetailsUpdateViewModel(database: AppDatabase) :
    BoardGameDetailsViewModel(database) {

    private val rank = MutableLiveData<NewestRankView?>()

    fun getRank(): LiveData<NewestRankView?> = rank

    fun loadTarget(id: Long) {
        viewModelScope.launch {
            loadBoardGameDetails(id)
            loadAvailableArtists()
            loadAvailableDesigners()
            loadAvailableLocations()
        }
    }

    override suspend fun persistBoardGame(boardGame: BoardGame) {
        database.boardGameDao().updateBoardGame(boardGame)
    }

    override suspend fun persistLocationRelation(locationRelation: BoardGameLocationRelation) {
        database.boardGameLocationRelationDao().updateRelation(locationRelation)
    }

    private suspend fun loadBoardGameDetails(id: Long) {
        val boardGameDetails = database.boardGameDao().getBoardGameDetails(id)
        boardGame.postValue(boardGameDetails.boardGame)
        artists.postValue(boardGameDetails.artists)
        designers.postValue(boardGameDetails.designers)
        rank.postValue(boardGameDetails.rank)
        locationRelation.postValue(
            BoardGameLocationRelation(
                boardGameDetails.boardGame.id,
                boardGameDetails.location?.id,
                boardGameDetails.location?.comment
            )
        )
    }
}