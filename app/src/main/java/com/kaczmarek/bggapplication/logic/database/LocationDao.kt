package com.kaczmarek.bggapplication.logic.database

import androidx.room.*
import com.kaczmarek.bggapplication.entities.database.Location

@Dao
interface LocationDao {

    @Query("SELECT * FROM Location")
    suspend fun getAllLocations(): List<Location>

    @Insert
    suspend fun addLocation(location: Location)

    @Update
    suspend fun updateLocation(location: Location)

    @Delete
    suspend fun deleteLocation(location: Location)
}