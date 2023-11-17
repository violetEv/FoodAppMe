package com.ackerman.foodappme.presentation.feature.splashscreen

import com.ackerman.foodappme.data.repository.UserRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SplashScreenViewModelTest {
    @MockK
    lateinit var userRepo: UserRepository

    @MockK
    private lateinit var viewModel: SplashScreenViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = SplashScreenViewModel(userRepo)
    }

    @Test
    fun testLoggedIn() {
        every { userRepo.isLoggedIn() } returns true
        val result = viewModel.isUserLoggedIn()
        verify { userRepo.isLoggedIn() }
        assertTrue(result)
    }
}
