package com.ackerman.foodappme.data.repository

import com.ackerman.foodappme.data.local.database.datasource.CartDataSource
import com.ackerman.foodappme.data.local.database.entity.CartEntity
import com.ackerman.foodappme.data.local.database.mapper.toCartEntity
import com.ackerman.foodappme.data.local.database.mapper.toCartList
import com.ackerman.foodappme.data.network.api.datasource.FoodAppDataSource
import com.ackerman.foodappme.data.network.api.model.order.OrderItemRequest
import com.ackerman.foodappme.data.network.api.model.order.OrderRequest
import com.ackerman.foodappme.model.Cart
import com.ackerman.foodappme.model.Menu
import com.ackerman.foodappme.utils.ResultWrapper
import com.ackerman.foodappme.utils.proceed
import com.ackerman.foodappme.utils.proceedFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

interface CartRepository {
    fun getUserCardData(): Flow<ResultWrapper<Pair<List<Cart>, Double>>>
    suspend fun createCart(product: Menu, totalQuantity: Int): Flow<ResultWrapper<Boolean>>
    suspend fun decreaseCart(item: Cart): Flow<ResultWrapper<Boolean>>
    suspend fun increaseCart(item: Cart): Flow<ResultWrapper<Boolean>>
    suspend fun setCartNotes(item: Cart): Flow<ResultWrapper<Boolean>>
    suspend fun deleteCart(item: Cart): Flow<ResultWrapper<Boolean>>
    suspend fun order(item: List<Cart>): Flow<ResultWrapper<Boolean>>
    suspend fun deleteAll()
}

class CartRepositoryImpl(
    private val dataSource: CartDataSource,
    private val foodAppDataSource: FoodAppDataSource
) : CartRepository {
    override suspend fun deleteAll() {
        dataSource.deleteAll()
    }

    override fun getUserCardData(): Flow<ResultWrapper<Pair<List<Cart>, Double>>> {
        return dataSource.getAllCarts()
            .map {
                proceed {
                    val result = it.toCartList()
                    val totalPrice = result.sumOf {
                        val pricePerItem = it.menuPrice
                        val quantity = it.itemQuantity
                        pricePerItem * quantity
                    }
                    Pair(result, totalPrice)
                }
            }.map {
                if (it.payload?.first?.isEmpty() == true) {
                    ResultWrapper.Empty(it.payload)
                } else {
                    it
                }
            }
            .onStart {
                emit(ResultWrapper.Loading())
                delay(2000)
            }
    }

    override suspend fun createCart(
        menu: Menu,
        totalQuantity: Int
    ): Flow<ResultWrapper<Boolean>> {
        return menu.id?.let { menuId ->
            proceedFlow {
                val affectedRow = dataSource.insertCart(
                    CartEntity(
                        menuId = menuId,
                        itemQuantity = totalQuantity,
                        imgMenuUrl = menu.imgMenuUrl,
                        menuName = menu.name,
                        menuPrice = menu.price
                    )
                )
                affectedRow > 0
            }
        } ?: flow {
            emit(ResultWrapper.Error(IllegalStateException("Menu ID not found")))
        }
    }

    override suspend fun decreaseCart(item: Cart): Flow<ResultWrapper<Boolean>> {
        val modifiedCart = item.copy().apply {
            itemQuantity -= 1
        }
        return if (modifiedCart.itemQuantity <= 0) {
            proceedFlow { dataSource.deleteCart(modifiedCart.toCartEntity()) > 0 }
        } else {
            proceedFlow { dataSource.updateCart(modifiedCart.toCartEntity()) > 0 }
        }
    }

    override suspend fun increaseCart(item: Cart): Flow<ResultWrapper<Boolean>> {
        val modifiedCart = item.copy().apply {
            itemQuantity += 1
        }
        return proceedFlow { dataSource.updateCart(modifiedCart.toCartEntity()) > 0 }
    }

    override suspend fun setCartNotes(item: Cart): Flow<ResultWrapper<Boolean>> {
        return proceedFlow { dataSource.updateCart(item.toCartEntity()) > 0 }
    }

    override suspend fun deleteCart(item: Cart): Flow<ResultWrapper<Boolean>> {
        return proceedFlow { dataSource.deleteCart(item.toCartEntity()) > 0 }
    }

    override suspend fun order(item: List<Cart>): Flow<ResultWrapper<Boolean>> {
        return proceedFlow {
            val orderItems = item.map {
                OrderItemRequest(it.itemNotes, it.menuId, it.itemQuantity)
            }
            val orderRequest = OrderRequest(orderItems)
            foodAppDataSource.createOrder(orderRequest).status == true
        }
    }
}
