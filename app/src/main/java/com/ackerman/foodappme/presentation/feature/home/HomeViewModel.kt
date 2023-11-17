package com.ackerman.foodappme.presentation.feature.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ackerman.foodappme.data.repository.MenuRepository
import com.ackerman.foodappme.data.repository.UserRepository
import com.ackerman.foodappme.model.Menu
import com.ackerman.foodappme.utils.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: MenuRepository, val repo: UserRepository) :
    ViewModel() {
    val categories = repository.getCategories().asLiveData(Dispatchers.IO)
    private val _menus = MutableLiveData<ResultWrapper<List<Menu>>>()
    val menus: LiveData<ResultWrapper<List<Menu>>>
        get() = _menus

    fun getCurrentUser() = repo.getCurrentUser()

    fun getMenus(category: String? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getMenus(if (category == "all") null else category).collect {
                _menus.postValue(it)
            }
        }
    }
}
