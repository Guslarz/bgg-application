package com.kaczmarek.bggapplication.application.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.withTransaction
import com.kaczmarek.bggapplication.entities.database.LocationWithBoardGameOverviews
import com.kaczmarek.bggapplication.logic.database.AppDatabase
import kotlinx.coroutines.launch

class LocationUpdateViewModel(database: AppDatabase) : BggViewModel(database) {

    private val location = MutableLiveData<LocationWithBoardGameOverviews>()

    fun getLocation(): LiveData<LocationWithBoardGameOverviews> = location

    fun loadTarget(id: Long) {
        viewModelScope.launch {
            loadLocation(id)
        }
    }

    fun commit() {
        viewModelScope.launch {
            database.withTransaction {
                database.locationDao().updateLocation(location.value!!.location)
            }
        }
    }

    private suspend fun loadLocation(id: Long) {
        location.postValue(database.locationDao().getLocationWithBoardGames(id))
    }
}