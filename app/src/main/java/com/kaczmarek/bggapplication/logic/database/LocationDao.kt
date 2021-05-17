package com.kaczmarek.bggapplication.logic.database

import androidx.room.*
import com.kaczmarek.bggapplication.entities.database.Location
import com.kaczmarek.bggapplication.entities.database.LocationWithBoardGameCount
import com.kaczmarek.bggapplication.entities.database.LocationBoardGameOverview

@Dao
interface LocationDao {

    @Query("SELECT * FROM Location")
    suspend fun getAllLocations(): List<Location>

    @Query("SELECT * FROM LocationWithBoardGameCount ORDER BY name")
    suspend fun getAllLocationsWithBoardGameCount(): List<LocationWithBoardGameCount>

    @Transaction
    @Query("SELECT * FROM BoardGameLocationRelation WHERE locationId = :locationId")
    suspend fun getLocationBoardGames(locationId: Long): List<LocationBoardGameOverview>

    @Insert
    suspend fun addLocation(location: Location): Long

    @Update
    suspend fun updateLocation(location: Location)

    @Delete
    suspend fun deleteLocation(location: Location)
}