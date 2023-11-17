package com.ackerman.foodappme.data.repository

import app.cash.turbine.test
import com.ackerman.foodappme.data.network.api.datasource.FoodAppDataSource
import com.ackerman.foodappme.data.network.api.model.category.CategoriesResponse
import com.ackerman.foodappme.data.network.api.model.category.CategoryResponse
import com.ackerman.foodappme.data.network.api.model.menu.MenuItemResponse
import com.ackerman.foodappme.data.network.api.model.menu.MenusResponse
import com.ackerman.foodappme.utils.ResultWrapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.lang.IllegalStateException

class MenuRepositoryImplTest {
    @MockK
    lateinit var remoteDataSource: FoodAppDataSource
    private lateinit var repository: MenuRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = MenuRepositoryImpl(remoteDataSource)
    }

    @Test
    fun `get categories, with result loading`() {
        val mockCategoryResponse = mockk<CategoriesResponse>()
        runTest {
            coEvery { remoteDataSource.getCategories() } returns mockCategoryResponse
            repository.getCategories().map {
                delay(100)
                it
            }.test {
                delay(110)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                coVerify { remoteDataSource.getCategories() }
            }
        }
    }

    @Test
    fun `get categories, with result success`() {
        val fakeCategoryResponse = CategoryResponse(
            id = "1",
            imgUrl = "url",
            name = "name",
            slug = "slug"
        )
        val fakeCategoriesResponse = CategoriesResponse(
            code = 200,
            status = true,
            message = "Success",
            data = listOf(fakeCategoryResponse)
        )
        runTest {
            coEvery { remoteDataSource.getCategories() } returns fakeCategoriesResponse
            repository.getCategories().map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                Assert.assertEquals(data.payload?.size, 1)
                Assert.assertEquals(data.payload?.get(0)?.id, "1")
                coVerify { remoteDataSource.getCategories() }
            }
        }
    }

    @Test
    fun `get categories, with result error`() {
        runTest {
            coEvery { remoteDataSource.getCategories() } throws IllegalStateException("Mock error")
            repository.getCategories().map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Error)
                coVerify { remoteDataSource.getCategories() }
            }
        }
    }

    @Test
    fun `get menus, with result loading`() {
        val mockMenuResponse = mockk<MenusResponse>()
        runTest {
            coEvery { remoteDataSource.getMenus(any()) } returns mockMenuResponse
            repository.getMenus("food").map {
                delay(100)
                it
            }.test {
                delay(110)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                coVerify { remoteDataSource.getMenus(any()) }
            }
        }
    }

    @Test
    fun `get menus, with result success`() {
        val fakeMenuItemResponse = MenuItemResponse(
            desc = "desc",
            id = 1,
            name = "name",
            price = 12000.0,
            formatPrice = "Rp.12000",
            resAddress = "Bandung",
            imgMenuUrl = "url"
        )
        val fakeMenusResponse = MenusResponse(
            code = 200,
            status = true,
            message = "Success",
            data = listOf(fakeMenuItemResponse)
        )
        runTest {
            coEvery { remoteDataSource.getMenus(any()) } returns fakeMenusResponse
            repository.getMenus("foods").map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                Assert.assertEquals(data.payload?.size, 1)
                Assert.assertEquals(data.payload?.get(0)?.id, 1)
                coVerify { remoteDataSource.getMenus(any()) }
            }
        }
    }

    @Test
    fun `get menus, with result empty`() {
        val fakeMenusResponse = MenusResponse(
            code = 200,
            status = true,
            message = "Success",
            data = emptyList()
        )
        runTest {
            coEvery { remoteDataSource.getMenus(any()) } returns fakeMenusResponse
            repository.getMenus().map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Empty)
                coVerify { remoteDataSource.getMenus(any()) }
            }
        }
    }

    @Test
    fun `get menus, with result error`() {
        runTest {
            coEvery { remoteDataSource.getMenus(any()) } throws IllegalStateException("Mock error")
            repository.getMenus().map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Error)
                coVerify { remoteDataSource.getMenus(any()) }
            }
        }
    }
}
