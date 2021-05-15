package com.kaczmarek.bggapplication.logic.database

import androidx.room.*
import com.kaczmarek.bggapplication.entities.database.Artist

@Dao
interface ArtistDao {

    @Query("SELECT * FROM Artist")
    suspend fun getAllArtists(): List<Artist>

    @Insert
    suspend fun addArtist(artist: Artist)

    @Update
    suspend fun updateArtist(artist: Artist)

    @Delete
    suspend fun deleteArtist(artist: Artist)
}