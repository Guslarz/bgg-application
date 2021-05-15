package com.kaczmarek.bggapplication.logic.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kaczmarek.bggapplication.entities.database.*

@Database(
    entities = [
        Artist::class, BoardGame::class, Designer::class, Rank::class, Location::class,
        BoardGamesArtistsRelation::class, BoardGamesDesignersRelation::class,
        BoardGameLocationRelation::class],
    views = [BoardGameNewestRankView::class, BoardGameLocationView::class,
        BoardGameOverview::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        private val LOCK = Any()
        private const val DB_NAME = "bgg-application"

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(LOCK) {
                INSTANCE ?: Room
                    .databaseBuilder(context.applicationContext, AppDatabase::class.java, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build()
            }
        }
    }

    abstract fun artistDao(): ArtistDao

    abstract fun boardGameDao(): BoardGameDao

    abstract fun designerDao(): DesignerDao

    abstract fun locationDao(): LocationDao

    abstract fun rankDao(): RankDao
}