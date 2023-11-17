package com.ackerman.foodappme.presentation.feature.profile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ackerman.foodappme.data.model.User
import com.ackerman.foodappme.data.repository.UserRepository
import com.ackerman.foodappme.tools.MainCoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class ProfileViewModelTest {
    @MockK
    private lateinit var repository: UserRepository

    private lateinit var viewModel: ProfileViewModel

    @get:Rule
    val testRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineRule: TestRule = MainCoroutineRule(UnconfinedTestDispatcher())

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        viewModel = spyk(ProfileViewModel(repo = repository))
        every { repository.getCurrentUser() } returns mockk(relaxed = true)
    }

    @Test
    fun `test logout`() {
        every { repository.doLogout() } returns true
        val result = viewModel.doLogout()
        assertEquals(result, result)
        verify { repository.doLogout() }
    }

    @Test
    fun getCurrentUser() {
        val user = User("name", "url", "email@gmail.com")
        every { repository.getCurrentUser() } returns user
        val result = viewModel.getCurrentUser()
        assertTrue(result == user)
        verify { repository.getCurrentUser() }
    }
}
