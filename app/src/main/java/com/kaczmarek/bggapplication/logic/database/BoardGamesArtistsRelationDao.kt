package com.kaczmarek.bggapplication.logic.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import com.kaczmarek.bggapplication.entities.database.BoardGamesArtistsRelation

@Dao
interface BoardGamesArtistsRelationDao {

    @Insert
    fun addRelation(relation: BoardGamesArtistsRelation)

    @Delete
    fun deleteRelation(relation: BoardGamesArtistsRelation)
}