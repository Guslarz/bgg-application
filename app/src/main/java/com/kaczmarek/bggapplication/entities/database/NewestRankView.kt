package com.kaczmarek.bggapplication.entities.database

import androidx.room.DatabaseView

@DatabaseView("SELECT bggId, value AS rank FROM Rank r WHERE datetime = (SELECT MAX(datetime) FROM Rank r2 WHERE r.bggId = r2.bggId)")
data class NewestRankView(
    val bggId: Long,
    val rank: Long?
)
