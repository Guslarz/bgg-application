package com.kaczmarek.bggapplication.application.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kaczmarek.bggapplication.logic.database.AppDatabase

class BggViewModelFactory(private val database: AppDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(BoardGameOverviewListViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return BoardGameOverviewListViewModel(database) as T
            }
            modelClass.isAssignableFrom(BoardGameDetailsUpdateViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return BoardGameDetailsUpdateViewModel(database) as T
            }
            modelClass.isAssignableFrom(BoardGameDetailsInsertViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return BoardGameDetailsInsertViewModel(database) as T
            }
            modelClass.isAssignableFrom(BggBoardGameSearchViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return BggBoardGameSearchViewModel(database) as T
            }
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}