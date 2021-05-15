package com.kaczmarek.bggapplication.entities.bggapi

data class BggBoardGameCollectionItem(
    val id: Long,
    val title: String,
    val yearPublished: Int,
    val rank: Long?,
    val comment: String?
)