package com.kaczmarek.bggapplication.logic.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.kaczmarek.bggapplication.entities.database.Rank

@Dao
interface RankDao {

    @Query("SELECT * FROM Rank WHERE bggId = :bggId")
    suspend fun getBoardGameRanks(bggId: Long): List<Rank>

    @Insert
    suspend fun addRank(rank: Rank)
}