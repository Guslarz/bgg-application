package com.kaczmarek.bggapplication.entities.database

import androidx.room.Entity
import java.time.LocalDateTime

@Entity(
    primaryKeys = ["bggId", "datetime"]
)
data class Rank(
    val bggId: Long,
    val datetime: LocalDateTime,
    val value: Long
)
