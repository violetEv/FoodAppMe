package com.ackerman.foodappme.presentation.feature.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ackerman.foodappme.data.local.datastore.UserPreferenceDataSource
import com.ackerman.foodappme.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers

class MainViewModel(private val repo:UserRepository) : ViewModel() {
    fun getCurrentUser() = repo.getCurrentUser()
    fun createChangePwdRequest(){
        repo.sendChangePasswordRequestByEmail()
    }
    fun doLogout(){
        repo.doLogout()
    }
}