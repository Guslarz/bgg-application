package com.kaczmarek.bggapplication.entities.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    primaryKeys = ["boardGameId", "artistId"],
    foreignKeys = [
        ForeignKey(
            entity = BoardGame::class,
            parentColumns = ["id"],
            childColumns = ["boardGameId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Artist::class,
            parentColumns = ["id"],
            childColumns = ["artistId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("artistId")]
)
data class BoardGamesArtistsRelation(
    val boardGameId: Long,
    val artistId: Long
)
