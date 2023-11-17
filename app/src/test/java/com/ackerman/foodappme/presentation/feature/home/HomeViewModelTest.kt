package com.ackerman.foodappme.presentation.feature.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ackerman.foodappme.data.model.User
import com.ackerman.foodappme.data.repository.MenuRepository
import com.ackerman.foodappme.data.repository.UserRepository
import com.ackerman.foodappme.tools.MainCoroutineRule
import com.ackerman.foodappme.tools.getOrAwaitValue
import com.ackerman.foodappme.utils.ResultWrapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class HomeViewModelTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineRule: TestRule = MainCoroutineRule(UnconfinedTestDispatcher())

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    lateinit var menuRepo: MenuRepository

    @MockK
    lateinit var userRepo: UserRepository

    @MockK
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        coEvery { menuRepo.getMenus(any()) } returns flow {
            emit(
                ResultWrapper.Success(
                    listOf(
                        mockk(relaxed = true),
                        mockk(relaxed = true),
                        mockk(relaxed = true)
                    )
                )
            )
        }

        coEvery { menuRepo.getCategories() } returns flow {
            emit(
                ResultWrapper.Success(
                    listOf(
                        mockk(relaxed = true),
                        mockk(relaxed = true),
                        mockk(relaxed = true),
                        mockk(relaxed = true)
                    )
                )
            )
        }

        coEvery { userRepo.getCurrentUser() } returns User("fullName", "url", "email")

        viewModel = spyk(
            HomeViewModel(
                repository = menuRepo,
                repo = userRepo
            )
        )
    }

    @Test
    fun `get categories`() {
        val result = viewModel.categories.getOrAwaitValue()
        assertEquals(result.payload?.size, 4)
        verify { menuRepo.getCategories() }
    }

    @Test
    fun `get menu`() {
        viewModel.getMenus()
        val result = viewModel.menus.getOrAwaitValue()
        assertEquals(result.payload?.size, 3)
        verify { menuRepo.getMenus() }
    }

    @Test
    fun getCurrentUser() {
        val user = User("name", "url", "email@gmail.com")
        every { userRepo.getCurrentUser() } returns user
        val result = viewModel.getCurrentUser()
        assertTrue(result == user)
    }
}
