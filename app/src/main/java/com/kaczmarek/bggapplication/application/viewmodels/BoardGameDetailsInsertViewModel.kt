package com.kaczmarek.bggapplication.application.viewmodels

import androidx.lifecycle.viewModelScope
import com.kaczmarek.bggapplication.R
import com.kaczmarek.bggapplication.entities.bggapi.BggApiResponse
import com.kaczmarek.bggapplication.entities.bggapi.BggBoardGameDetails
import com.kaczmarek.bggapplication.entities.bggapi.BggBoardGameOverview
import com.kaczmarek.bggapplication.entities.database.BoardGame
import com.kaczmarek.bggapplication.entities.database.BoardGameLocationRelation
import com.kaczmarek.bggapplication.logic.bggapi.BggApiDao
import com.kaczmarek.bggapplication.logic.database.AppDatabase
import kotlinx.coroutines.launch

class BoardGameDetailsInsertViewModel(database: AppDatabase) :
    BoardGameDetailsViewModel(database) {

    fun loadTarget(overview: BggBoardGameOverview?) {
        viewModelScope.launch {
            setIsLoading(true)

            try {
                if (overview != null) {
                    val details = loadGameDetails(overview.id)
                    boardGame.postValue(BoardGame(overview, details))
                } else {
                    boardGame.postValue(BoardGame())
                }
                artists.postValue(listOf())
                designers.postValue(listOf())
                locationRelation.postValue(
                    BoardGameLocationRelation(0)
                )
            } catch (e: Exception) {
                setErrorMessage(R.string.err_bgg_api_request_failed)
            }

            setIsLoading(false)
        }
    }

    override suspend fun persistBoardGame(boardGame: BoardGame) {
        this.boardGame.value!!.id = database.boardGameDao().addBoardGame(boardGame)
        locationRelation.value!!.boardGameId = boardGame.id
    }

    override suspend fun persistLocationRelation(locationRelation: BoardGameLocationRelation) {
        database.boardGameLocationRelationDao().addRelation(locationRelation)
    }

    private suspend fun loadGameDetails(id: Long): BggBoardGameDetails {
        val response = BggApiDao().getGameDetails(id)
        if (response is BggApiResponse.Success) {
            return response.data
        }
        throw RuntimeException()
    }
}