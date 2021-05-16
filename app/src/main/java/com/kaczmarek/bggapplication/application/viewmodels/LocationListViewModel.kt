package com.kaczmarek.bggapplication.application.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kaczmarek.bggapplication.entities.database.Location
import com.kaczmarek.bggapplication.entities.database.LocationWithBoardGameCount
import com.kaczmarek.bggapplication.logic.database.AppDatabase
import kotlinx.coroutines.launch

class LocationListViewModel(database: AppDatabase) : BggViewModel(database) {

    private val locationList: MutableLiveData<List<LocationWithBoardGameCount>> by lazy {
        MutableLiveData<List<LocationWithBoardGameCount>>().also { loadLocationList() }
    }

    fun getLocationList(): LiveData<List<LocationWithBoardGameCount>> = locationList

    fun addLocation(location: Location) {
        viewModelScope.launch {
            database.locationDao().addLocation(location)
            loadLocationList()
        }
    }

    fun removeLocation(location: Location) {
        viewModelScope.launch {
            database.locationDao().deleteLocation(location)
            loadLocationList()
        }
    }

    private fun loadLocationList() {
        viewModelScope.launch {
            val list = database.locationDao().getAllLocationsWithBoardGameCount()
            locationList.postValue(list)
        }
    }
}