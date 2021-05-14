package com.kaczmarek.bggapplication.logic.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.kaczmarek.bggapplication.entities.internal.BoardGameDetails
import com.kaczmarek.bggapplication.entities.internal.BoardGameOverview

@Dao
interface BoardGameDao {

    @Query("SELECT * FROM BoardGameOverview ORDER BY yearPublished ASC")
    suspend fun getBoardGameOverviewsYearAsc(): List<BoardGameOverview>

    @Query("SELECT * FROM BoardGameOverview ORDER BY yearPublished DESC")
    suspend fun getBoardGameOverviewsYearDesc(): List<BoardGameOverview>

    @Query("SELECT * FROM BoardGameOverview ORDER BY rank ASC")
    suspend fun getBoardGameOverviewsRankAsc(): List<BoardGameOverview>

    @Query("SELECT * FROM BoardGameOverview ORDER BY rank DESC")
    suspend fun getBoardGameOverviewsRankDesc(): List<BoardGameOverview>

    @Transaction
    @Query("SELECT * FROM BoardGame WHERE id = :id")
    suspend fun getBoardGameDetails(id: Long): BoardGameDetails
}