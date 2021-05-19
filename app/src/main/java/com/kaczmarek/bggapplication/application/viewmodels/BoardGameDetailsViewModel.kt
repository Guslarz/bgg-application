package com.kaczmarek.bggapplication.application.viewmodels

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.withTransaction
import com.kaczmarek.bggapplication.R
import com.kaczmarek.bggapplication.entities.database.*
import com.kaczmarek.bggapplication.logic.database.AppDatabase
import kotlinx.coroutines.launch

abstract class BoardGameDetailsViewModel(database: AppDatabase) : BggViewModel(database) {

    protected val boardGame = MutableLiveData<BoardGame>()
    protected val artists = MutableLiveData<List<Artist>>()
    protected val designers = MutableLiveData<List<Designer>>()
    protected val locationRelation = MutableLiveData<BoardGameLocationRelation>()
    private val availableArtists = MutableLiveData<List<Artist>>()
    private val availableDesigners = MutableLiveData<List<Designer>>()
    private val availableLocations = MutableLiveData<List<Location>>()
    @Volatile
    private var queries = mutableListOf<() -> Unit>()

    protected abstract suspend fun persistBoardGame(boardGame: BoardGame)
    protected abstract suspend fun persistLocationRelation(
        locationRelation: BoardGameLocationRelation
    )

    fun getBoardGame(): LiveData<BoardGame> = boardGame
    fun getDesigners(): LiveData<List<Designer>> = designers
    fun getArtists(): LiveData<List<Artist>> = artists
    fun getLocationRelation(): LiveData<BoardGameLocationRelation> = locationRelation
    fun getAvailableArtists(): LiveData<List<Artist>> = availableArtists
    fun getAvailableDesigners(): LiveData<List<Designer>> = availableDesigners
    fun getAvailableLocations(): LiveData<List<Location>> = availableLocations

    fun commit(callback: () -> Unit) {
        val locationRelationValue = locationRelation.value!!
        viewModelScope.launch {
            if (locationRelationValue.locationId == null)
                locationRelationValue.comment = ""

            database.withTransaction {
                try {
                    persistBoardGame(boardGame.value!!)
                    persistLocationRelation(locationRelationValue)
                    for (query in queries)
                        query()
                    callback()
                } catch (e: SQLiteConstraintException) {
                    setErrorMessage(R.string.err_invalid_value)
                }
            }
            queries = mutableListOf()
        }
    }

    fun addArtist(artist: Artist) {
        if (artists.value!!.contains(artist)) {
            setErrorMessage(R.string.err_artist_already_added)
            return
        }
        artists.value = artists.value!!.plusElement(artist)
        queries.add {
            database.boardGamesArtistsRelationDao().addRelation(
                BoardGamesArtistsRelation(
                    boardGame.value!!.id,
                    artist.id
                )
            )
        }
    }

    fun addDesigner(designer: Designer) {
        if (designers.value!!.contains(designer)) {
            setErrorMessage(R.string.err_designer_already_added)
            return
        }
        designers.value = designers.value!!.plusElement(designer)
        queries.add {
            database.boardGamesDesignersRelationDao().addRelation(
                BoardGamesDesignersRelation(
                    boardGame.value!!.id,
                    designer.id
                )
            )
        }
    }

    fun removeArtist(artist: Artist) {
        artists.value = artists.value!!.minusElement(artist)
        queries.add {
            database.boardGamesArtistsRelationDao().deleteRelation(
                BoardGamesArtistsRelation(
                    boardGame.value!!.id,
                    artist.id
                )
            )
        }
    }

    fun removeDesigner(designer: Designer) {
        designers.value = designers.value!!.minusElement(designer)
        queries.add {
            database.boardGamesDesignersRelationDao().deleteRelation(
                BoardGamesDesignersRelation(
                    boardGame.value!!.id,
                    designer.id
                )
            )
        }
    }

    protected suspend fun loadAvailableArtists() {
        availableArtists.postValue(database.artistDao().getAllArtists())
    }

    protected suspend fun loadAvailableDesigners() {
        availableDesigners.postValue(database.designerDao().getAllDesigners())
    }

    protected suspend fun loadAvailableLocations() {
        availableLocations.postValue(database.locationDao().getAllLocations())
    }
}