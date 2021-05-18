package com.kaczmarek.bggapplication.application.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.withTransaction
import com.kaczmarek.bggapplication.R
import com.kaczmarek.bggapplication.entities.bggapi.BggApiResponse
import com.kaczmarek.bggapplication.entities.bggapi.BggBoardGameCollectionItem
import com.kaczmarek.bggapplication.entities.bggapi.BggBoardGameDetails
import com.kaczmarek.bggapplication.entities.database.BoardGame
import com.kaczmarek.bggapplication.entities.database.BoardGameLocationRelation
import com.kaczmarek.bggapplication.entities.database.Rank
import com.kaczmarek.bggapplication.logic.bggapi.BggApiDao
import com.kaczmarek.bggapplication.logic.database.AppDatabase
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.RuntimeException
import java.time.LocalDateTime

class BggUserCollectionViewModel(database: AppDatabase) : BggViewModel(database) {

    @Volatile
    private var userCollection: List<BggBoardGameCollectionItem>? = null
    private val hasCollection = MutableLiveData<Boolean>()

    fun getHasCollection(): LiveData<Boolean> = hasCollection

    fun searchByUsername(username: String) {
        viewModelScope.launch {
            hasCollection.postValue(false)
            setIsLoading(true)
            try {
                userCollection = loadUserCollection(username)
                hasCollection.postValue(true)
            } catch (e: Exception) {
                setErrorMessage(R.string.err_bgg_api_request_failed)
            }
            setIsLoading(false)
        }
    }

    fun importCollection() {
        viewModelScope.launch {
            setIsLoading(true)
            database.withTransaction {
                for (item in userCollection!!) {
                    addItem(item)
                }
            }
            setIsLoading(false)
        }
    }

    fun updateRanking() {
        val collection = userCollection!!
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
    }

    private suspend fun loadItemDetails(id: Long): BggBoardGameDetails {
        val response = BggApiDao().getGameDetails(id)
        if (response is BggApiResponse.Success) {
            return response.data
        }
        throw RuntimeException()
    }

    private suspend fun updateRankingFromCollection(
        collection: List<BggBoardGameCollectionItem>, datetime: LocalDateTime) {

        for (item in collection) {
            if (item.rank != null && database.boardGameDao().checkBoardGameExists(item.id)) {
                database.rankDao().addRank(Rank(item.id, datetime, item.rank))
            }
        }
    }

    private suspend fun updateRemainingRanking(
        collection: List<BggBoardGameCollectionItem>, datetime: LocalDateTime) {

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