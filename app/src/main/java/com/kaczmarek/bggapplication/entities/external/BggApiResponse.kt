package com.kaczmarek.bggapplication.entities.external

sealed class BggApiResponse<out R> {
    data class Success<out T>(val data: T) : BggApiResponse<T>()
    data class Error(val exception: Exception) : BggApiResponse<Nothing>()
}