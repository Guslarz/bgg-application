package com.kaczmarek.bggapplication.entities.internal

import androidx.room.DatabaseView

@DatabaseView(
    "SELECT BoardGame.id, BoardGame.title, BoardGame.yearPublished, " +
            "BoardGame.thumbnail, RecentRank.value AS rank " +
            "FROM BoardGame " +
            "JOIN (" +
            "SELECT boardGameId, value " +
            "FROM Rank " +
            "INNER JOIN (" +
            "SELECT boardGameId, MAX(date) " +
            "FROM Rank " +
            "GROUP BY boardGameId) " +
            "USING(boardGameId)) RecentRank " +
            "ON BoardGame.id = RecentRank.boardGameId"
)
data class BoardGameOverview(
    val id: Long,
    val title: String,
    val yearPublished: Int,
    val thumbnail: String?,
    val rank: Long?
)