package com.ackerman.foodappme.presentation.feature.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ackerman.foodappme.data.dummy.DummyMenuDataSourceImpl
import com.ackerman.foodappme.model.Menu
import com.ackerman.foodappme.presentation.feature.home.adapter.AdapterLayoutMode

class HomeViewModel : ViewModel() {

    private val _menuListLiveData = MutableLiveData<List<Menu>>()
    val menuListLiveData: LiveData<List<Menu>>
        get() = _menuListLiveData

    private val _layoutModeLiveData = MutableLiveData<AdapterLayoutMode>()
    val layoutModeLiveData: LiveData<AdapterLayoutMode>
        get() = _layoutModeLiveData


    fun updateMenuList(menuList: List<Menu>) {
        _menuListLiveData.value = menuList
    }

    fun updateLayoutMode(layoutMode: AdapterLayoutMode) {
        _layoutModeLiveData.value = layoutMode
    }
}