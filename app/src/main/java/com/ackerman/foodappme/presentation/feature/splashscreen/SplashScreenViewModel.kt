package com.ackerman.foodappme.presentation.feature.splashscreen

import androidx.lifecycle.ViewModel
import com.ackerman.foodappme.data.repository.UserRepository

class SplashScreenViewModel(private val repository: UserRepository) : ViewModel() {
    fun isUserLoggedIn() = repository.isLoggedIn()
}
