package com.kaczmarek.bggapplication.logic.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.kaczmarek.bggapplication.entities.database.Rank

@Dao
interface RankDao {

    @Query("SELECT * FROM Rank WHERE boardGameId = :boardGameId")
    suspend fun getBoardGameRanks(boardGameId: Long): List<Rank>

    @Insert
    suspend fun addRank(rank: Rank)
}