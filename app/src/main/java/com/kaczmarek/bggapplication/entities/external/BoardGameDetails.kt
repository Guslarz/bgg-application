package com.kaczmarek.bggapplication.entities.external

data class BoardGameDetails(
    val type: String,
    val originalTitle: String,
    val yearPublished: Int,
    val designers: List<String>,
    val artists: List<String>,
    val description: String
)
