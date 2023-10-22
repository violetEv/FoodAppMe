package com.ackerman.foodappme.presentation.feature.profile

import androidx.lifecycle.ViewModel
import com.ackerman.foodappme.data.repository.UserRepository

class ProfileViewModel(private val repo:UserRepository) : ViewModel() {
    fun getCurrentUser() = repo.getCurrentUser()

    fun doLogout(){
        repo.doLogout()
    }
}