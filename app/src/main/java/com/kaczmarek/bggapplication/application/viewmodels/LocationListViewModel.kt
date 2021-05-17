package com.kaczmarek.bggapplication.application.viewmodels

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kaczmarek.bggapplication.R
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
            try {
                location.id = database.locationDao().addLocation(location)
                locationList.postValue(
                    locationList.value!!.plusElement(
                        LocationWithBoardGameCount(location, 0)
                    )
                )
            } catch (e: SQLiteConstraintException) {
                setErrorMessage(R.string.err_location_name_duplicate)
            }
        }
    }

    fun removeLocation(location: LocationWithBoardGameCount) {
        viewModelScope.launch {
            database.locationDao().deleteLocation(location.location)
            locationList.postValue(locationList.value!!.minusElement(location))
        }
    }

    fun refresh() {
        loadLocationList()
    }

    private fun loadLocationList() {
        viewModelScope.launch {
            val list = database.locationDao().getAllLocationsWithBoardGameCount()
            locationList.postValue(list)
        }
    }
}