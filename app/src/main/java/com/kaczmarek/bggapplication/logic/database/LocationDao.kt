package com.kaczmarek.bggapplication.logic.database

import androidx.room.*
import com.kaczmarek.bggapplication.entities.database.Location
import com.kaczmarek.bggapplication.entities.database.LocationWithBoardGameCount
import com.kaczmarek.bggapplication.entities.database.LocationWithBoardGameOverviews

@Dao
interface LocationDao {

    @Query("SELECT * FROM Location")
    suspend fun getAllLocations(): List<Location>

    @Query("SELECT * FROM LocationWithBoardGameCount")
    suspend fun getAllLocationsWithBoardGameCount(): List<LocationWithBoardGameCount>

    @Transaction
    @Query("SELECT * FROM BoardGameLocationRelation WHERE locationId = :locationId")
    fun getLocationWithBoardGames(locationId: Long): LocationWithBoardGameOverviews

    @Insert
    suspend fun addLocation(location: Location)

    @Update
    suspend fun updateLocation(location: Location)

    @Delete
    suspend fun deleteLocation(location: Location)
}