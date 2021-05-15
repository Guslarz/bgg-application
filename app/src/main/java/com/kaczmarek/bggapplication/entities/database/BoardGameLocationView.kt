package com.kaczmarek.bggapplication.entities.database

import androidx.room.DatabaseView

@DatabaseView(
    "SELECT BoardGame.id, Location.name, BoardGameLocationRelation.comment " +
            "FROM BoardGame " +
            "INNER JOIN BoardGameLocationRelation " +
            "ON BoardGame.id = boardGameId " +
            "INNER JOIN Location " +
            "ON locationId = Location.id"
)
data class BoardGameLocationView(
    val id: Long,
    val name: String,
    val comment: String?
)
