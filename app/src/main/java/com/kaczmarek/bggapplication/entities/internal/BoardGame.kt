package com.kaczmarek.bggapplication.entities.internal

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BoardGame(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val title: String,
    val originalTitle: String,
    val yearPublished: Int,
    val description: String,
    val thumbnail: String?,
    val comment: String?
)