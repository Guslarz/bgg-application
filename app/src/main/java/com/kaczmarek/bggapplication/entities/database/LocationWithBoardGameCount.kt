package com.kaczmarek.bggapplication.entities.database

import androidx.room.DatabaseView
import androidx.room.Embedded

@DatabaseView(
    "SELECT * " +
            "FROM Location " +
            "LEFT JOIN (" +
            "SELECT locationId AS id, COUNT(*) AS boardGameCount " +
            "FROM BoardGameLocationRelation " +
            "GROUP BY locationId) " +
            "USING(id)"
)
data class LocationWithBoardGameCount(
    @Embedded val location: Location,
    val boardGameCount: Int
)