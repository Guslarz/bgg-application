package com.kaczmarek.bggapplication.logic.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kaczmarek.bggapplication.entities.internal.*

@Database(
    entities = [
        Artist::class, BoardGame::class, Designer::class, Rank::class,
        BoardGamesArtistsRelation::class, BoardGamesDesignersRelation::class],
    views = [BoardGameOverview::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        @Volatile private var instance: AppDatabase? = null
        private val LOCK = Any()
        private const val DB_NAME = "bgg-application"

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(LOCK) {
                instance ?: Room
                    .databaseBuilder(context.applicationContext, AppDatabase::class.java, DB_NAME)
                    .build()
            }
        }
    }

    abstract fun boardGameDao(): BoardGameDao
}