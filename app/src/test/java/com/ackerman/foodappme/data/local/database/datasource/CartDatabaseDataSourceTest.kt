package com.ackerman.foodappme.data.local.database.datasource

import app.cash.turbine.test
import com.ackerman.foodappme.data.local.database.dao.CartDao
import com.ackerman.foodappme.data.local.database.entity.CartEntity
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CartDatabaseDataSourceTest {

    @MockK
    lateinit var cartDao: CartDao

    private lateinit var cartDataSource: CartDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        cartDataSource = CartDatabaseDataSource(cartDao)
    }

    @Test
    fun getAllCarts() {
        val itemEntityMock1 = mockk<CartEntity>()
        val itemEntityMock2 = mockk<CartEntity>()
        val listEntityMock = listOf(itemEntityMock1, itemEntityMock2)
        val flowMock = flow {
            emit(listEntityMock)
        }
        coEvery { cartDao.getAllCarts() } returns flowMock
        runTest {
            cartDataSource.getAllCarts().test {
                val result = awaitItem()
                assertEquals(listEntityMock, result)
                assertEquals(listEntityMock.size, result.size)
                assertEquals(itemEntityMock1, result[0])
                assertEquals(itemEntityMock2, result[1])
                awaitComplete()
            }
        }
    }

    @Test
    fun getCartById() {
        val mockItemEntity = mockk<CartEntity>()
        val flowMock = flow {
            emit(mockItemEntity)
        }
        coEvery { cartDao.getCartById(any()) } returns flowMock
        runTest {
            cartDataSource.getCartById(1).test {
                assertEquals(mockItemEntity, awaitItem())
                awaitComplete()
            }
        }
    }

    @Test
    fun insertCart() {
        runTest {
            val mockCartEntity = mockk<CartEntity>()
            coEvery { cartDao.insertCart(any()) } returns 1
            val result = cartDataSource.insertCart(mockCartEntity)
            coVerify { cartDao.insertCart(any()) }
            assertEquals(result, 1)
        }
    }

    @Test
    fun deleteCart() {
        runTest {
            val mockCartEntity = mockk<CartEntity>()
            coEvery { cartDao.deleteMenu(any()) } returns 1
            val result = cartDataSource.deleteCart(mockCartEntity)
            coVerify { cartDao.deleteMenu(any()) }
            assertEquals(result, 1)
        }
    }

    @Test
    fun updateCart() {
        runTest {
            val mockCartEntity = mockk<CartEntity>()
            coEvery { cartDao.updateMenu(any()) } returns 1
            val result = cartDataSource.updateCart(mockCartEntity)
            coVerify { cartDao.updateMenu(any()) }
            assertEquals(result, 1)
        }
    }

    @Test
    fun deleteAll() {
        runTest {
            coEvery { cartDao.deleteAll() } returns Unit
            val result = cartDataSource.deleteAll()
            coVerify { cartDao.deleteAll() }
            assertEquals(result, Unit)
        }
    }
}
