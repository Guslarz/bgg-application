package com.kaczmarek.bggapplication.application.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kaczmarek.bggapplication.logic.database.AppDatabase

class BggViewModelFactory(private val database: AppDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BoardGameOverviewListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BoardGameOverviewListViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}