package com.ackerman.foodappme.presentation.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ackerman.foodappme.data.repository.MenuRepository
import com.ackerman.foodappme.model.Menu
import com.ackerman.foodappme.utils.ResultWrapper
import kotlinx.coroutines.Dispatchers

class HomeViewModel (private val menuRepository: MenuRepository) : ViewModel() {
    val menuList = menuRepository.getMenus().asLiveData(Dispatchers.IO)
    private val _data = MutableLiveData<ResultWrapper<Boolean>>()
    val data: LiveData<ResultWrapper<Boolean>>
        get() = _data

}