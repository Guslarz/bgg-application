package com.kaczmarek.bggapplication.entities.internal

import androidx.room.Entity
import androidx.room.ForeignKey
import java.time.LocalDate

@Entity(
    primaryKeys = ["boardGameId", "date"],
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
    val date: LocalDate,
    val value: Long
)
