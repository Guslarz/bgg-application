package com.kaczmarek.bggapplication.logic.database

import androidx.room.*
import com.kaczmarek.bggapplication.entities.database.BoardGame
import com.kaczmarek.bggapplication.entities.database.BoardGameDetails
import com.kaczmarek.bggapplication.entities.database.BoardGameOverview

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

    @Insert
    suspend fun addBoardGame(boardGame: BoardGame)

    @Update
    suspend fun updateBoardGame(boardGame: BoardGame)

    @Delete
    suspend fun deleteBoardGame(boardGame: BoardGame)
}