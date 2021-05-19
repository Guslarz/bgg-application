package com.kaczmarek.bggapplication.application.viewmodels

import androidx.lifecycle.viewModelScope
import androidx.room.withTransaction
import com.kaczmarek.bggapplication.R
import com.kaczmarek.bggapplication.entities.bggapi.BggApiResponse
import com.kaczmarek.bggapplication.entities.bggapi.BggBoardGameDetails
import com.kaczmarek.bggapplication.entities.bggapi.BggBoardGameOverview
import com.kaczmarek.bggapplication.entities.database.*
import com.kaczmarek.bggapplication.logic.bggapi.BggApiDao
import com.kaczmarek.bggapplication.logic.database.AppDatabase
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

class BoardGameDetailsInsertViewModel(database: AppDatabase) :
    BoardGameDetailsViewModel(database) {

    var details: BggBoardGameDetails? = null

    fun loadTarget(overview: BggBoardGameOverview?) {
        viewModelScope.launch {
            setIsLoading(true)

            try {
                if (overview != null) {
                    details = loadGameDetails(overview.id)
                    boardGame.postValue(BoardGame(overview, details!!))
                    database.withTransaction {
                        artists.postValue(addArtistsFromDetails(details!!.artists))
                        designers.postValue(addDesignersFromDetails(details!!.designers))
                    }
                } else {
                    boardGame.postValue(BoardGame())
                    artists.postValue(listOf())
                    designers.postValue(listOf())
                }
                locationRelation.postValue(
                    BoardGameLocationRelation(0)
                )
                loadAvailableArtists()
                loadAvailableDesigners()
                loadAvailableLocations()
            } catch (e: Exception) {
                setErrorMessage(R.string.err_bgg_api_request_failed)
            }

            setIsLoading(false)
        }
    }

    override suspend fun persistBoardGame(boardGame: BoardGame) {
        boardGame.id = database.boardGameDao().addBoardGame(boardGame)
        locationRelation.value!!.boardGameId = boardGame.id
        val rank = details?.rank
        if (rank != null) {
            database.rankDao().addRank(Rank(boardGame.bggId!!, LocalDateTime.now(), rank))
        }
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

    private suspend fun addArtistsFromDetails(artistNames: List<String>): List<Artist> {
        val artists = mutableListOf<Artist>()
        for (artistName in artistNames) {
            var artist = database.artistDao().getArtistByName(artistName)
            if (artist == null) {
                artist = Artist(0, artistName)
                artist.id = database.artistDao().addArtist(artist)
            }
            artists.add(artist)
        }
        return artists
    }

    private suspend fun addDesignersFromDetails(designerNames: List<String>): List<Designer> {
        val designers = mutableListOf<Designer>()
        for (designerName in designerNames) {
            var designer = database.designerDao().getDesignerByName(designerName)
            if (designer == null) {
                designer = Designer(0, designerName)
                designer.id = database.designerDao().addDesigner(designer)
            }
            designers.add(designer)
        }
        return designers
    }
}