package com.kaczmarek.bggapplication.entities.bggapi

data class BggBoardGameDetails(
    val title: String,
    val type: String,
    val thumbnail: String?,
    val designers: List<String>,
    val artists: List<String>,
    val description: String,
    val rank: Long?
)
