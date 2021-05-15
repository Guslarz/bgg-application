package com.kaczmarek.bggapplication.logic.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import com.kaczmarek.bggapplication.entities.database.BoardGameLocationRelation

@Dao
interface BoardGameLocationRelationDao {

    @Insert
    fun addRelation(relation: BoardGameLocationRelation)

    @Update
    fun updateRelation(relation: BoardGameLocationRelation)
}