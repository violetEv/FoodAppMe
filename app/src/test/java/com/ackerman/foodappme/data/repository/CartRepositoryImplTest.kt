package com.ackerman.foodappme.data.repository

import app.cash.turbine.test
import com.ackerman.foodappme.data.local.database.datasource.CartDataSource
import com.ackerman.foodappme.data.local.database.entity.CartEntity
import com.ackerman.foodappme.data.network.api.datasource.FoodAppDataSource
import com.ackerman.foodappme.data.network.api.model.order.OrderResponse
import com.ackerman.foodappme.model.Cart
import com.ackerman.foodappme.model.Menu
import com.ackerman.foodappme.utils.ResultWrapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CartRepositoryImplTest {
    @MockK
    lateinit var localDataSource: CartDataSource

    @MockK
    lateinit var remoteDataSource: FoodAppDataSource
    private lateinit var repository: CartRepository

    private val fakeCartList = listOf(
        CartEntity(
            id = 1,
            menuId = 1,
            menuName = "Ayam geprek",
            menuPrice = 12000.0,
            imgMenuUrl = "url",
            itemQuantity = 2,
            itemNotes = "notes"
        ),
        CartEntity(
            id = 2,
            menuId = 2,
            menuName = "Ayam bakar",
            menuPrice = 14000.0,
            imgMenuUrl = "url",
            itemQuantity = 2,
            itemNotes = "notes"
        )
    )
    val mockCart = Cart(
        id = 1,
        menuId = 1,
        menuName = "Ayam",
        menuPrice = 12000.0,
        imgMenuUrl = "url",
        itemQuantity = 2,
        itemNotes = "notes"
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = CartRepositoryImpl(localDataSource, remoteDataSource)
    }

    @Test
    fun deleteAll() {
        coEvery { localDataSource.deleteAll() } returns Unit
        runTest {
            val result = repository.deleteAll()
            coVerify { localDataSource.deleteAll() }
            assertEquals(result, Unit)
        }
    }

    @Test
    fun `get user card data, result success`() {
        every { localDataSource.getAllCarts() } returns flow {
            emit(fakeCartList)
        }
        runTest {
            repository.getUserCardData().map {
                delay(100)
                it
            }.test {
                delay(2201)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                assertEquals(data.payload?.first?.size, 2)
                assertEquals(data.payload?.second, 52000.0)
                verify { localDataSource.getAllCarts() }
            }
        }
    }

    @Test
    fun `get user card data, result loading`() {
        every { localDataSource.getAllCarts() } returns flow {
            emit(fakeCartList)
        }
        runTest {
            repository.getUserCardData().map {
                delay(100)
                it
            }.test {
                delay(2101)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                verify { localDataSource.getAllCarts() }
            }
        }
    }

    @Test
    fun `get user card data, result error`() {
        every { localDataSource.getAllCarts() } returns flow {
            throw IllegalStateException("Mock Error")
        }
        runTest {
            repository.getUserCardData().map {
                delay(100)
                it
            }.test {
                delay(2201)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Error)
                verify { localDataSource.getAllCarts() }
            }
        }
    }

    @Test
    fun `get user card data, result empty`() {
        every { localDataSource.getAllCarts() } returns flow {
            emit(listOf())
        }
        runTest {
            repository.getUserCardData().map {
                delay(100)
                it
            }.test {
                delay(2201)
                val data = expectMostRecentItem()
                Assert.assertTrue(data is ResultWrapper.Empty)
                verify { localDataSource.getAllCarts() }
            }
        }
    }

    @Test
    fun `create cart loading,menu id not null`() {
        runTest {
            val mockMenu = mockk<Menu>(relaxed = true)
            coEvery { localDataSource.insertCart(any()) } returns 1
            repository.createCart(mockMenu, 1)
                .map {
                    delay(100)
                    it
                }.test {
                    delay(110)
                    val result = expectMostRecentItem()
                    Assert.assertTrue(result is ResultWrapper.Loading)
                    coVerify { localDataSource.insertCart(any()) }
                }
        }
    }

    @Test
    fun `create cart success,menu id not null`() {
        runTest {
            val mockMenu = mockk<Menu>(relaxed = true)
            coEvery { localDataSource.insertCart(any()) } returns 1
            repository.createCart(mockMenu, 1)
                .map {
                    delay(100)
                    it
                }.test {
                    delay(210)
                    val result = expectMostRecentItem()
                    Assert.assertTrue(result is ResultWrapper.Success)
                    Assert.assertEquals(result.payload, true)
                    coVerify { localDataSource.insertCart(any()) }
                }
        }
    }

    @Test
    fun `create cart error, menu id not null`() {
        runTest {
            val mockMenu = mockk<Menu>(relaxed = true)
            coEvery { localDataSource.insertCart(any()) } throws IllegalStateException("Mock Error")
            repository.createCart(mockMenu, 1)
                .map {
                    delay(100)
                    it
                }.test {
                    delay(210)
                    val result = expectMostRecentItem()
                    assertTrue(result is ResultWrapper.Error)
                    coVerify { localDataSource.insertCart(any()) }
                }
        }
    }

    @Test
    fun `create cart error, menu id null`() {
        runTest {
            val mockMenu = mockk<Menu>(relaxed = true) {
                every { id } returns null
            }
            coEvery { localDataSource.insertCart(any()) } returns 1
            repository.createCart(mockMenu, 1)
                .map {
                    delay(100)
                    it
                }.test {
                    delay(210)
                    val result = expectMostRecentItem()
                    assertTrue(result is ResultWrapper.Error)
                    assertEquals(result.exception?.message, "Menu ID not found")
                    coVerify(atLeast = 0) { localDataSource.insertCart(any()) }
                }
        }
    }

    @Test
    fun `decrease cart when quantity less than or equal 0`() {
        val mockCart = Cart(
            id = 1,
            menuId = 1,
            menuName = "Ayam",
            menuPrice = 12000.0,
            imgMenuUrl = "url",
            itemQuantity = 0,
            itemNotes = "notes"
        )
        coEvery { localDataSource.deleteCart(any()) } returns 1
        coEvery { localDataSource.updateCart(any()) } returns 1
        runTest {
            repository.decreaseCart(mockCart).map {
                delay(100)
                it
            }.test {
                delay(210)
                val result = expectMostRecentItem()
                assertEquals(result.payload, true)
                coVerify(atLeast = 1) { localDataSource.deleteCart(any()) }
                coVerify(atLeast = 0) { localDataSource.updateCart(any()) }
            }
        }
    }

    @Test
    fun `decrease cart when quantity more than 0`() {
        val mockCart = Cart(
            id = 1,
            menuId = 1,
            menuName = "Ayam",
            menuPrice = 12000.0,
            imgMenuUrl = "url",
            itemQuantity = 2,
            itemNotes = "notes"
        )
        coEvery { localDataSource.deleteCart(any()) } returns 1
        coEvery { localDataSource.updateCart(any()) } returns 1
        runTest {
            repository.decreaseCart(mockCart).map {
                delay(100)
                it
            }.test {
                delay(210)
                val result = expectMostRecentItem()
                Assert.assertEquals(result.payload, true)
                coVerify(atLeast = 0) { localDataSource.deleteCart(any()) }
                coVerify(atLeast = 1) { localDataSource.updateCart(any()) }
            }
        }
    }

    @Test
    fun `increase cart`() {
        coEvery { localDataSource.updateCart(any()) } returns 1
        runTest {
            repository.increaseCart(mockCart).map {
                delay(100)
                it
            }.test {
                delay(210)
                val result = expectMostRecentItem()
                Assert.assertEquals(result.payload, true)
                coVerify(atLeast = 1) { localDataSource.updateCart(any()) }
            }
        }
    }

    @Test
    fun `set cart notes`() {
        coEvery { localDataSource.updateCart(any()) } returns 1
        runTest {
            repository.setCartNotes(mockCart).map {
                delay(100)
                it
            }.test {
                delay(210)
                val result = expectMostRecentItem()
                Assert.assertEquals(result.payload, true)
                coVerify(atLeast = 1) { localDataSource.updateCart(any()) }
            }
        }
    }

    @Test
    fun `delete cart`() {
        coEvery { localDataSource.deleteCart(any()) } returns 1
        runTest {
            repository.deleteCart(mockCart).map {
                delay(100)
                it
            }.test {
                delay(210)
                val result = expectMostRecentItem()
                Assert.assertEquals(result.payload, true)
                coVerify(atLeast = 1) { localDataSource.deleteCart(any()) }
            }
        }
    }

    @Test
    fun `test order`() {
        runTest {
            val mockCarts = listOf(
                Cart(
                    id = 1,
                    menuId = 1,
                    menuName = "Ayam bakar",
                    menuPrice = 12000.0,
                    imgMenuUrl = "url",
                    itemQuantity = 2,
                    itemNotes = "notes"
                ),
                Cart(
                    id = 2,
                    menuId = 2,
                    menuName = "Ayam geprek",
                    menuPrice = 12000.0,
                    imgMenuUrl = "url",
                    itemQuantity = 2,
                    itemNotes = "notes"
                )
            )
            coEvery { remoteDataSource.createOrder(any()) } returns OrderResponse(
                code = 200,
                message = "Success",
                status = true
            )
            repository.order(mockCarts).map {
                delay(100)
                it
            }.test {
                delay(210)
                val result = expectMostRecentItem()
                Assert.assertTrue(result is ResultWrapper.Success)
            }
        }
    }
}
