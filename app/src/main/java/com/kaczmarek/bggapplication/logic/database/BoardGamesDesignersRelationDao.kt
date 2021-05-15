package com.kaczmarek.bggapplication.logic.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import com.kaczmarek.bggapplication.entities.database.BoardGamesDesignersRelation

@Dao
interface BoardGamesDesignersRelationDao {

    @Insert
    fun addRelation(relation: BoardGamesDesignersRelation)

    @Delete
    fun deleteRelation(relation: BoardGamesDesignersRelation)
}