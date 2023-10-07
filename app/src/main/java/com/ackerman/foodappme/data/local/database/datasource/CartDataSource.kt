package com.ackerman.foodappme.data.local.database.datasource

import com.ackerman.foodappme.data.local.database.dao.CartDao
import com.ackerman.foodappme.data.local.database.entity.CartEntity
import com.ackerman.foodappme.data.local.database.relation.CartMenuRelation
import kotlinx.coroutines.flow.Flow

interface CartDataSource {
    fun getAllCarts(): Flow<List<CartMenuRelation>>
    fun getCartById(cartId: Int): Flow<CartMenuRelation>
    suspend fun insertCart(cart: CartEntity) : Long
    suspend fun deleteCart(cart: CartEntity): Int
    suspend fun updateCart(cart: CartEntity): Int
}

class CartDatabaseDataSource(private val cartDao: CartDao) : CartDataSource {
    override fun getAllCarts(): Flow<List<CartMenuRelation>> {
        return cartDao.getAllCarts()
    }

    override fun getCartById(cartId: Int): Flow<CartMenuRelation> {
        return cartDao.getCartById(cartId)
    }

    override suspend fun insertCart(cart: CartEntity): Long {
        return cartDao.insertCart(cart)
    }

    override suspend fun deleteCart(cart: CartEntity): Int {
        return cartDao.deleteMenu(cart)
    }

    override suspend fun updateCart(cart: CartEntity): Int {
        return cartDao.updateMenu(cart)
    }

}