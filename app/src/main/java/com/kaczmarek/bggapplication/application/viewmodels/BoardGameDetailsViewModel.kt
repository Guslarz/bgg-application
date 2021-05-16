package com.kaczmarek.bggapplication.application.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.withTransaction
import com.kaczmarek.bggapplication.entities.database.*
import com.kaczmarek.bggapplication.logic.database.AppDatabase
import kotlinx.coroutines.launch

abstract class BoardGameDetailsViewModel(database: AppDatabase) : BggViewModel(database) {

    protected val boardGame = MutableLiveData<BoardGame>()
    protected val artists = MutableLiveData<List<Artist>>()
    protected val designers = MutableLiveData<List<Designer>>()
    protected val locationRelation = MutableLiveData<BoardGameLocationRelation>()
    private val availableArtists: MutableLiveData<List<Artist>> by lazy {
        MutableLiveData<List<Artist>>().also { loadAvailableArtists() }
    }
    private val availableDesigners: MutableLiveData<List<Designer>> by lazy {
        MutableLiveData<List<Designer>>().also { loadAvailableDesigners() }
    }
    private val availableLocations: MutableLiveData<List<Location>> by lazy {
        MutableLiveData<List<Location>>().also { loadAvailableLocations() }
    }
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

    fun commit() {
        viewModelScope.launch {
            if (locationRelation.value!!.locationId == null)
                locationRelation.value!!.comment = ""

            database.withTransaction {
                persistBoardGame(boardGame.value!!)
                persistLocationRelation(locationRelation.value!!)
                for (query in queries)
                    query()
            }
            queries = mutableListOf()
        }
    }

    fun addArtist(artist: Artist) {
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

    private fun loadAvailableArtists() {
        viewModelScope.launch {
            availableArtists.postValue(database.artistDao().getAllArtists())
        }
    }

    private fun loadAvailableDesigners() {
        viewModelScope.launch {
            availableDesigners.postValue(database.designerDao().getAllDesigners())
        }
    }

    private fun loadAvailableLocations() {
        viewModelScope.launch {
            availableLocations.postValue(database.locationDao().getAllLocations())
        }
    }
}