package com.kaczmarek.bggapplication.entities.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = BoardGame::class,
            parentColumns = ["id"],
            childColumns = ["boardGameId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Location::class,
            parentColumns = ["id"],
            childColumns = ["locationId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index("locationId")]
)
data class BoardGameLocationRelation(
    @PrimaryKey var boardGameId: Long,
    var locationId: Long?,
    var comment: String?
) {

    companion object {
        private const val DEFAULT_COMMENT = ""
    }

    constructor(boardGameId: Long) : this(
        boardGameId = boardGameId,
        locationId = null,
        comment = DEFAULT_COMMENT
    )
}
