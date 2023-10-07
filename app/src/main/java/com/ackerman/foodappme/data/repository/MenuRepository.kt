package com.ackerman.foodappme.data.repository

import com.ackerman.foodappme.data.dummy.DummyCategoryDataSource
import com.ackerman.foodappme.data.local.database.datasource.MenuDataSource
import com.ackerman.foodappme.data.local.database.mapper.toMenuList
import com.ackerman.foodappme.model.Category
import com.ackerman.foodappme.model.Menu
import com.ackerman.foodappme.utils.ResultWrapper
import com.ackerman.foodappme.utils.proceed
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart


interface MenuRepository {
    fun getCategories(): List<Category>
    fun getMenus(): Flow<ResultWrapper<List<Menu>>>
}

class MenuRepositoryImpl(
    private val menuDataSource: MenuDataSource,
    private val dummyCategoryDataSource: DummyCategoryDataSource
) : MenuRepository {
    override fun getCategories(): List<Category> {
        return dummyCategoryDataSource.getMenuCategory()
    }

    override fun getMenus(): Flow<ResultWrapper<List<Menu>>> {
        return menuDataSource.getAllMenus().map { proceed { it.toMenuList() } }.onStart {
            emit(ResultWrapper.Loading())
            delay(2000)
        }
    }
}