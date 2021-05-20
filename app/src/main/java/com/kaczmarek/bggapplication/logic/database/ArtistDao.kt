package com.kaczmarek.bggapplication.logic.database

import androidx.room.*
import com.kaczmarek.bggapplication.entities.database.Artist

@Dao
interface ArtistDao {

    @Query("SELECT * FROM Artist ORDER BY name")
    suspend fun getAllArtists(): List<Artist>

    @Query("SELECT * FROM Artist WHERE name=:name")
    suspend fun getArtistByName(name: String): Artist?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addArtist(artist: Artist): Long

    @Update
    suspend fun updateArtist(artist: Artist)

    @Delete
    suspend fun deleteArtist(artist: Artist)
}