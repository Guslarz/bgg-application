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

    @Query("SELECT EXISTS(SELECT 1 FROM BoardGame WHERE id = :id)")
    suspend fun checkBoardGameExists(id: Long): Boolean

    @Query("SELECT id FROM BoardGame WHERE id NOT IN (:ids)")
    suspend fun getIdsExcept(ids: List<Long>): List<Long>

    @Query("SELECT * FROM BoardGameOverview WHERE id IN (:ids)")
    suspend fun getOverviewsByIds(ids: List<Long>): List<BoardGameOverview>

    @Insert
    suspend fun addBoardGame(boardGame: BoardGame): Long

    @Update
    suspend fun updateBoardGame(boardGame: BoardGame)

    @Query("DELETE FROM BoardGame WHERE id = :id")
    suspend fun deleteBoardGameById(id: Long)
}