package com.ackerman.foodappme.presentation.feature.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ackerman.foodappme.data.repository.CartRepository
import com.ackerman.foodappme.tools.MainCoroutineRule
import com.ackerman.foodappme.tools.getOrAwaitValue
import com.ackerman.foodappme.utils.ResultWrapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class CartViewModelTest {
    @MockK
    private lateinit var repository: CartRepository
    private lateinit var viewModel: CartViewModel

    @get:Rule
    val testRule: TestRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineRule: TestRule = MainCoroutineRule(UnconfinedTestDispatcher())

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        coEvery { repository.getUserCardData() } returns flow {
            emit(
                ResultWrapper.Success(
                    Pair(
                        listOf(
                            mockk(relaxed = true),
                            mockk(relaxed = true)
                        ),
                        127000.0
                    )
                )
            )
        }
        viewModel = spyk(CartViewModel(repository))
        val updateResultMock = flow {
            emit(ResultWrapper.Success(true))
        }
        coEvery { repository.decreaseCart(any()) } returns updateResultMock
        coEvery { repository.increaseCart(any()) } returns updateResultMock
        coEvery { repository.deleteCart(any()) } returns updateResultMock
        coEvery { repository.setCartNotes(any()) } returns updateResultMock
    }

    @Test
    fun `test cart list`() {
        val result = viewModel.cartList.getOrAwaitValue()
        assertEquals(result.payload?.first?.size, 2)
        assertEquals(result.payload?.second, 127000.0)
    }

    @Test
    fun `test decrease cart`() {
        viewModel.decreaseCart(mockk())
        coVerify { repository.decreaseCart(any()) }
    }

    @Test
    fun `test increase cart`() {
        viewModel.increaseCart(mockk())
        coVerify { repository.increaseCart(any()) }
    }

    @Test
    fun `test remove cart`() {
        viewModel.removeCart(mockk())
        coVerify { repository.deleteCart(any()) }
    }

    @Test
    fun `test set cart notes`() {
        viewModel.setCartNotes(mockk())
        coVerify { repository.setCartNotes(any()) }
    }
}
