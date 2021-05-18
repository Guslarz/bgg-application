package com.kaczmarek.bggapplication.entities.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Designer(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String
) {

    override fun toString(): String = name
}
