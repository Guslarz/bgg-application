package com.kaczmarek.bggapplication.entities.database

import androidx.room.DatabaseView
import androidx.room.Embedded

@DatabaseView(
    "SELECT BoardGame.id, Location.id AS location_id, " +
            "Location.name AS location_name, BoardGameLocationRelation.comment " +
            "FROM BoardGame " +
            "INNER JOIN BoardGameLocationRelation " +
            "ON BoardGame.id = boardGameId " +
            "INNER JOIN Location " +
            "ON locationId = Location.id"
)
data class BoardGameLocationView(
    val id: Long,
    @Embedded(prefix = "location_") val location: Location,
    val comment: String
)
