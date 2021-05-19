package com.kaczmarek.bggapplication.entities.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [
        Index("name", unique = true)
    ]
)
data class Designer(
    @PrimaryKey(autoGenerate = true) var id: Long,
    val name: String
) {

    override fun toString(): String = name
}
