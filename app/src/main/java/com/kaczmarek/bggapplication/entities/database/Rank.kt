package com.kaczmarek.bggapplication.entities.database

import androidx.room.Entity
import androidx.room.ForeignKey
import java.time.LocalDateTime

@Entity(
    primaryKeys = ["boardGameId", "datetime"],
    foreignKeys = [
        ForeignKey(
            entity = BoardGame::class,
            parentColumns = ["id"],
            childColumns = ["boardGameId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Rank(
    val boardGameId: Long,
    val datetime: LocalDateTime,
    val value: Long
)
