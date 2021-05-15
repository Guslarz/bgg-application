package com.kaczmarek.bggapplication.entities.database

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class BoardGameDetails(
    @Embedded val boardGame: BoardGame,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            BoardGamesDesignersRelation::class,
            parentColumn = "boardGameId",
            entityColumn = "designerId"
        )
    )
    val designers: List<Designer>,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            BoardGamesArtistsRelation::class,
            parentColumn = "boardGameId",
            entityColumn = "artistId"
        )
    )
    val artists: List<Artist>,
    @Relation(
        parentColumn = "id",
        entityColumn = "id"
    )
    val ranks: BoardGameNewestRankView,
    @Relation(
        parentColumn = "id",
        entityColumn = "id"
    )
    val location: BoardGameLocationView
)
