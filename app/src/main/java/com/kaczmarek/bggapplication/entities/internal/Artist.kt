package com.kaczmarek.bggapplication.entities.internal

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Artist(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String
)
