package com.kaczmarek.bggapplication.entities.internal

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    primaryKeys = ["boardGameId", "designerId"],
    foreignKeys = [
        ForeignKey(
            entity = BoardGame::class,
            parentColumns = ["id"],
            childColumns = ["boardGameId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Designer::class,
            parentColumns = ["id"],
            childColumns = ["designerId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("designerId")]
)
data class BoardGamesDesignersRelation(
    val boardGameId: Long,
    val designerId: Long
)
