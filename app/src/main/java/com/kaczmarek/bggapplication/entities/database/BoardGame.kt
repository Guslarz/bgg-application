package com.kaczmarek.bggapplication.entities.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity
data class BoardGame(
    @PrimaryKey(autoGenerate = true) val id: Long,
    var title: String,
    var originalTitle: String,
    var yearPublished: Int,
    var description: String,
    var orderDate: LocalDate,
    var addToCollectionDate: LocalDate,
    var price: Double,
    var suggestedRetailPrice: Double,
    var codeEanUpc: String,
    var bggId: Long?,
    var productionCode: String,
    var type: BoardGameType,
    var comment: String?,
    var thumbnail: String?
)