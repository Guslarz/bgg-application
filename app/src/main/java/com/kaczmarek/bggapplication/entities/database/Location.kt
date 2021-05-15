package com.kaczmarek.bggapplication.entities.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Location(
    @PrimaryKey(autoGenerate = true) val id: Long,
    var name: String
)
