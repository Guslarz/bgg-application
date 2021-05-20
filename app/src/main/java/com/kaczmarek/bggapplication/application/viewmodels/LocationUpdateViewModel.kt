package com.kaczmarek.bggapplication.application.viewmodels

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.withTransaction
import com.kaczmarek.bggapplication.R
import com.kaczmarek.bggapplication.entities.database.Location
import com.kaczmarek.bggapplication.entities.database.LocationBoardGameOverview
import com.kaczmarek.bggapplication.logic.database.AppDatabase
import kotlinx.coroutines.launch

class LocationUpdateViewModel(database: AppDatabase) : BggViewModel(database) {

    private lateinit var location: Location
    private val locationBoardGames = MutableLiveData<List<LocationBoardGameOverview>>()

    fun getLocation(): Location = location
    fun getLocationBoardGames(): LiveData<List<LocationBoardGameOverview>> = locationBoardGames

    fun loadTarget(target: Location) {
        location = target
        viewModelScope.launch {
            loadLocationBoardGames(location.id)
        }
    }

    fun commit(callback: () -> Unit) {
        viewModelScope.launch {
            database.withTransaction {
                try {
                    database.locationDao().updateLocation(location)
                    callback()
                } catch (e: SQLiteConstraintException) {
                    setErrorMessage(R.string.err_invalid_value)
                }
            }
        }
    }

    private suspend fun loadLocationBoardGames(id: Long) {
        locationBoardGames.postValue(database.locationDao().getLocationBoardGames(id))
    }
}