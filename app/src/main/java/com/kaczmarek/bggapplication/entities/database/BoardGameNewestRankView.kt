package com.kaczmarek.bggapplication.entities.database

import androidx.room.DatabaseView

@DatabaseView(
    "SELECT boardGameId AS id, value AS rank " +
            "FROM Rank r " +
            "WHERE datetime = (" +
            "SELECT MAX(datetime) " +
            "FROM Rank r2 " +
            "WHERE r.boardGameId = r2.boardGameId)"
)
data class BoardGameNewestRankView(
    val id: Long,
    val rank: Long?
)
