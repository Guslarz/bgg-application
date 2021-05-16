package com.kaczmarek.bggapplication.entities.database

import androidx.room.Embedded
import androidx.room.Relation

data class LocationWithBoardGameOverviews(
    @Embedded val locationRelation: BoardGameLocationRelation,
    @Relation(
        parentColumn = "locationId",
        entityColumn = "id"
    )
    val location: Location,
    @Relation(
        parentColumn = "boardGameId",
        entityColumn = "id"
    )
    val boardGames: List<BoardGameOverview>
)