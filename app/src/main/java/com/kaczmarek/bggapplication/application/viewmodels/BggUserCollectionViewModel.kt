package com.kaczmarek.bggapplication.application.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.withTransaction
import com.kaczmarek.bggapplication.R
import com.kaczmarek.bggapplication.entities.bggapi.BggApiResponse
import com.kaczmarek.bggapplication.entities.bggapi.BggBoardGameCollectionItem
import com.kaczmarek.bggapplication.entities.bggapi.BggBoardGameDetails
import com.kaczmarek.bggapplication.entities.database.*
import com.kaczmarek.bggapplication.logic.bggapi.BggApiDao
import com.kaczmarek.bggapplication.logic.database.AppDatabase
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class BggUserCollectionViewModel(database: AppDatabase) : BggViewModel(database) {

    private val userCollection = MutableLiveData<List<BggBoardGameCollectionItem>>()
    private val hasCollection = MutableLiveData<Boolean>()

    fun getUserCollection(): LiveData<List<BggBoardGameCollectionItem>> = userCollection
    fun getHasCollection(): LiveData<Boolean> = hasCollection

    fun searchByUsername(username: String) {
        viewModelScope.launch {
            setIsLoading(true)
            try {
                userCollection.postValue(loadUserCollection(username))
                hasCollection.postValue(true)
            } catch (e: Exception) {
                setErrorMessage(R.string.err_bgg_api_request_failed)
                userCollection.postValue(listOf())
                hasCollection.postValue(false)
            }
            setIsLoading(false)
        }
    }

    fun importItem(item: BggBoardGameCollectionItem) {
        viewModelScope.launch {
            database.withTransaction {
                addItem(item)
            }
        }
    }

    fun updateRanking() {
        val collection = userCollection.value!!
        val datetime = LocalDateTime.now()
        viewModelScope.launch {
            setIsLoading(true)
            database.withTransaction {
                updateRankingFromCollection(collection, datetime)
                updateRemainingRanking(collection, datetime)
            }
            setIsLoading(false)
        }
    }

    private suspend fun loadUserCollection(username: String): List<BggBoardGameCollectionItem> {
        val response = BggApiDao().getCollectionOfUser(username)
        if (response is BggApiResponse.Success) {
            return response.data
        }
        throw RuntimeException()
    }

    private suspend fun addItem(item: BggBoardGameCollectionItem) {
        val details = loadItemDetails(item.id)
        val boardGame = BoardGame(item, details)
        val boardGameId = database.boardGameDao().addBoardGame(boardGame)

        val locationRelation = BoardGameLocationRelation(boardGameId)
        database.boardGameLocationRelationDao().addRelation(locationRelation)

        if (details.rank != null) {
            database.rankDao().addRank(Rank(item.id, LocalDateTime.now(), details.rank))
        }

        for (name in details.artists) {
            var artist = database.artistDao().getArtistByName(name)
            if (artist == null) {
                artist = Artist(0, name)
                artist.id = database.artistDao().addArtist(artist)
            }
            database.boardGamesArtistsRelationDao().addRelation(
                BoardGamesArtistsRelation(
                    boardGameId,
                    artist.id
                )
            )
        }

        for (name in details.designers) {
            var designer = database.designerDao().getDesignerByName(name)
            if (designer == null) {
                designer = Designer(0, name)
                designer.id = database.designerDao().addDesigner(designer)
            }
            database.boardGamesDesignersRelationDao().addRelation(
                BoardGamesDesignersRelation(
                    boardGameId,
                    designer.id
                )
            )
        }
    }

    private suspend fun loadItemDetails(id: Long): BggBoardGameDetails {
        val response = BggApiDao().getGameDetails(id)
        if (response is BggApiResponse.Success) {
            return response.data
        }
        throw RuntimeException()
    }

    private suspend fun updateRankingFromCollection(
        collection: List<BggBoardGameCollectionItem>, datetime: LocalDateTime
    ) {

        for (item in collection) {
            if (item.rank != null) {
                database.rankDao().addRank(Rank(item.id, datetime, item.rank))
            }
        }
    }

    private suspend fun updateRemainingRanking(
        collection: List<BggBoardGameCollectionItem>, datetime: LocalDateTime
    ) {

        val collectionIds = collection.map { it.id }
        val remainingIds = database.boardGameDao().getBggIdsExcept(collectionIds)
        val overviews = database.boardGameDao().getOverviewsByIds(remainingIds)
        for (overview in overviews) {
            if (overview.rank == null) {
                continue
            }
            val details = loadItemDetails(overview.id)
            if (details.rank != null) {
                database.rankDao().addRank(Rank(overview.id, datetime, details.rank))
            }
        }
    }
}