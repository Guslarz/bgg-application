package com.kaczmarek.bggapplication.entities.database

import androidx.room.Embedded
import androidx.room.Relation

data class LocationBoardGameOverview(
    @Embedded val locationRelation: BoardGameLocationRelation,
    @Relation(
        parentColumn = "boardGameId",
        entityColumn = "id"
    )
    val boardGame: BoardGameOverview
)