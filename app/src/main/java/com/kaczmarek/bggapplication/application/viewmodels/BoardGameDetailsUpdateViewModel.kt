package com.kaczmarek.bggapplication.application.viewmodels

import androidx.lifecycle.viewModelScope
import com.kaczmarek.bggapplication.entities.database.BoardGame
import com.kaczmarek.bggapplication.entities.database.BoardGameLocationRelation
import com.kaczmarek.bggapplication.logic.database.AppDatabase
import kotlinx.coroutines.launch

class BoardGameDetailsUpdateViewModel(database: AppDatabase) :
    BoardGameDetailsViewModel(database) {

    fun loadTarget(id: Long) {
        loadBoardGameDetails(id)
    }

    override suspend fun persistBoardGame(boardGame: BoardGame) {
        database.boardGameDao().updateBoardGame(boardGame)
    }

    override suspend fun persistLocationRelation(locationRelation: BoardGameLocationRelation) {
        database.boardGameLocationRelationDao().updateRelation(locationRelation)
    }

    private fun loadBoardGameDetails(id: Long) {
        viewModelScope.launch {
            val boardGameDetails = database.boardGameDao().getBoardGameDetails(id)
            boardGame.postValue(boardGameDetails.boardGame)
            artists.postValue(boardGameDetails.artists)
            designers.postValue(boardGameDetails.designers)
            boardGameDetails.run {
                locationRelation.postValue(
                    BoardGameLocationRelation(
                        boardGame.id,
                        location!!.id,
                        location.comment
                    )
                )
            }
        }
    }
}