package com.kaczmarek.bggapplication.logic

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

class ImageLoader(private val dispatcher: CoroutineDispatcher = Dispatchers.IO) {

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun loadFromUrl(url: String): Bitmap {
        return withContext(dispatcher) {
            val inputStream = URL(url).openStream()
            BitmapFactory.decodeStream(inputStream)
        }
    }
}