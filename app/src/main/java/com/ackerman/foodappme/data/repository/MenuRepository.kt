package com.ackerman.foodappme.data.repository

import com.ackerman.foodappme.data.network.api.datasource.FoodAppDataSource
import com.ackerman.foodappme.data.network.api.model.category.toCategoryList
import com.ackerman.foodappme.data.network.api.model.menu.toMenuList
import com.ackerman.foodappme.model.Category
import com.ackerman.foodappme.model.Menu
import com.ackerman.foodappme.utils.ResultWrapper
import com.ackerman.foodappme.utils.proceedFlow
import kotlinx.coroutines.flow.Flow

interface MenuRepository {
    fun getCategories(): Flow<ResultWrapper<List<Category>>>
    fun getMenus(category: String? = null): Flow<ResultWrapper<List<Menu>>>
}

class MenuRepositoryImpl(
    private val apiDataSource: FoodAppDataSource
) : MenuRepository {
    override fun getCategories(): Flow<ResultWrapper<List<Category>>> {
        return proceedFlow {
            apiDataSource.getCategories().data?.toCategoryList() ?: emptyList()
        }
    }

    override fun getMenus(category: String?): Flow<ResultWrapper<List<Menu>>> {
        return proceedFlow {
            apiDataSource.getMenus(category).data?.toMenuList() ?: emptyList()
        }
    }
}
