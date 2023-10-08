package com.ackerman.foodappme.presentation.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ackerman.foodappme.data.repository.MenuRepository
import kotlinx.coroutines.Dispatchers

class HomeViewModel (private val repository: MenuRepository): ViewModel (){

    val cartList = repository.getMenus().asLiveData(Dispatchers.IO)

}