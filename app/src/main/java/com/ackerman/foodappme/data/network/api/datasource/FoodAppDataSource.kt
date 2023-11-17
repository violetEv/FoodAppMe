package com.ackerman.foodappme.data.network.api.datasource

import com.ackerman.foodappme.data.network.api.model.category.CategoriesResponse
import com.ackerman.foodappme.data.network.api.model.menu.MenusResponse
import com.ackerman.foodappme.data.network.api.model.order.OrderRequest
import com.ackerman.foodappme.data.network.api.model.order.OrderResponse
import com.ackerman.foodappme.data.network.api.service.FoodAppApiService

interface FoodAppDataSource {
    suspend fun getMenus(category: String? = null): MenusResponse
    suspend fun getCategories(): CategoriesResponse
    suspend fun createOrder(orderRequest: OrderRequest): OrderResponse
}

class FoodAppApiDataSource(private val service: FoodAppApiService) : FoodAppDataSource {
    override suspend fun getMenus(category: String?): MenusResponse {
        return service.getMenus(category)
    }

    override suspend fun getCategories(): CategoriesResponse {
        return service.getCategories()
    }

    override suspend fun createOrder(orderRequest: OrderRequest): OrderResponse {
        return service.createOrder(orderRequest)
    }
}
