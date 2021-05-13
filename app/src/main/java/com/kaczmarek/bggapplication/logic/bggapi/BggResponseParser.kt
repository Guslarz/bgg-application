package com.kaczmarek.bggapplication.logic.bggapi

import java.io.InputStream

interface BggResponseParser<T> {
    fun parse(inputStream: InputStream): T
}