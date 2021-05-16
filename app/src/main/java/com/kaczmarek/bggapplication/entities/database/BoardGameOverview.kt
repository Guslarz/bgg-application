package com.kaczmarek.bggapplication.entities.database

import androidx.room.DatabaseView

@DatabaseView(
    "SELECT BoardGame.id, BoardGame.title, BoardGame.yearPublished, " +
            "BoardGame.thumbnail, BoardGame.description, BoardGameNewestRankView.rank " +
            "FROM BoardGame " +
            "JOIN BoardGameNewestRankView " +
            "USING(id)"
)
data class BoardGameOverview(
    val id: Long,
    val title: String,
    val yearPublished: Int,
    val thumbnail: String?,
    val description: String,
    val rank: Long?
)