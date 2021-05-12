package com.kaczmarek.bggapplication.entities.internal

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BoardGame(
    @PrimaryKey val id: Long,
    val title: String,
    val originalTitle: String,
    val publicationDate: Int,
    val description: String
)