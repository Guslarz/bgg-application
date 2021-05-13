package com.kaczmarek.bggapplication.entities.external

data class BggBoardGameCollectionItem(
    val id: Long,
    val title: String,
    val yearPublished: Int,
    val rank: Long?
)