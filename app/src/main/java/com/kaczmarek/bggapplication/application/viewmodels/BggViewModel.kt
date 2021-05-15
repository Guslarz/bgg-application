package com.kaczmarek.bggapplication.application.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kaczmarek.bggapplication.logic.database.AppDatabase

abstract class BggViewModel(protected val database: AppDatabase) : ViewModel() {

    private val isLoading = MutableLiveData<Boolean>()
    private val errorMessage = MutableLiveData<Int>()

    fun getIsLoading(): LiveData<Boolean> = isLoading
    fun getErrorMessage(): LiveData<Int> = errorMessage

    protected fun setIsLoading(isLoading: Boolean) {
        this.isLoading.postValue(isLoading)
    }

    protected fun setErrorMessage(errorMessage: Int) {
        this.errorMessage.postValue(errorMessage)
    }
}