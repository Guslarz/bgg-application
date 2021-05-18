package com.kaczmarek.bggapplication.entities.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.kaczmarek.bggapplication.entities.bggapi.BggBoardGameCollectionItem
import com.kaczmarek.bggapplication.entities.bggapi.BggBoardGameDetails
import com.kaczmarek.bggapplication.entities.bggapi.BggBoardGameOverview
import java.time.LocalDate

@Entity(
    indices = [
        Index("bggId")
    ]
)
data class BoardGame(
    @PrimaryKey(autoGenerate = true) var id: Long,
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
    var comment: String,
    var thumbnail: String?
) {

    companion object {
        private const val DEFAULT_PRICE = 0.0
        private const val DEFAULT_SUGGESTED_RETAIL_PRICE = 0.0
        private const val DEFAULT_CODE_EAN_UPC = ""
        private const val DEFAULT_PRODUCTION_CODE = ""
        private const val DEFAULT_COMMENT = ""
    }

    constructor(overview: BggBoardGameOverview, details: BggBoardGameDetails) : this(
        id = 0,
        title = overview.title,
        originalTitle = details.title,
        yearPublished = overview.yearPublished,
        description = details.description,
        orderDate = LocalDate.now(),
        addToCollectionDate = LocalDate.now(),
        price = DEFAULT_PRICE,
        suggestedRetailPrice = DEFAULT_SUGGESTED_RETAIL_PRICE,
        codeEanUpc = DEFAULT_CODE_EAN_UPC,
        bggId = overview.id,
        productionCode = DEFAULT_PRODUCTION_CODE,
        type = BoardGameType.GAME,
        comment = DEFAULT_COMMENT,
        thumbnail = details.thumbnail
    )

    constructor() : this(
        id = 0,
        title = "",
        originalTitle = "",
        yearPublished = LocalDate.now().year,
        description = "",
        orderDate = LocalDate.now(),
        addToCollectionDate = LocalDate.now(),
        price = DEFAULT_PRICE,
        suggestedRetailPrice = DEFAULT_SUGGESTED_RETAIL_PRICE,
        codeEanUpc = DEFAULT_CODE_EAN_UPC,
        bggId = null,
        productionCode = DEFAULT_PRODUCTION_CODE,
        type = BoardGameType.GAME,
        comment = DEFAULT_COMMENT,
        thumbnail = null
    )

    constructor(item: BggBoardGameCollectionItem, details: BggBoardGameDetails) : this(
        id = 0,
        title = item.title,
        originalTitle = details.title,
        yearPublished = item.yearPublished,
        description = details.description,
        orderDate = LocalDate.now(),
        addToCollectionDate = LocalDate.now(),
        price = DEFAULT_PRICE,
        suggestedRetailPrice = DEFAULT_SUGGESTED_RETAIL_PRICE,
        codeEanUpc = DEFAULT_CODE_EAN_UPC,
        bggId = item.id,
        productionCode = DEFAULT_PRODUCTION_CODE,
        type = BoardGameType.GAME,
        comment = item.comment ?: DEFAULT_COMMENT,
        thumbnail = details.thumbnail
    )
}