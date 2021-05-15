package com.kaczmarek.bggapplication.application

import android.app.Application
import com.kaczmarek.bggapplication.logic.database.AppDatabase

class BggApplication : Application() {
    val database by lazy { AppDatabase.getInstance(this) }
}