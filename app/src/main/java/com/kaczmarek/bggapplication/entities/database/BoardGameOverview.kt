package com.kaczmarek.bggapplication.entities.database

import androidx.room.DatabaseView

@DatabaseView(
    "SELECT BoardGame.id, BoardGame.title, BoardGame.yearPublished, BoardGame.thumbnail, NewestRankView.rank FROM BoardGame LEFT JOIN NewestRankView ON BoardGame.bggId = NewestRankView.bggId OR (BoardGame.bggId IS NULL AND NewestRankView.bggId IS NULL)"
)
data class BoardGameOverview(
    val id: Long,
    val title: String,
    val yearPublished: Int,
    val thumbnail: String?,
    val rank: Long?
)