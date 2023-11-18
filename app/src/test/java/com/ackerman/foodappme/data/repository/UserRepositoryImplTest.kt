package com.ackerman.foodappme.data.repository

import app.cash.turbine.test
import com.ackerman.foodappme.data.network.firebase.auth.FirebaseAuthDataSource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UserRepositoryImplTest {
    @MockK
    lateinit var dataSource: FirebaseAuthDataSource
    private lateinit var repository: UserRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = UserRepositoryImpl(dataSource)
    }

    @Test
    fun testLogin() {
        coEvery { dataSource.doLogin("email", "password") } returns true
        runTest {
            repository.doLogin("email", "password").map {
                delay(100)
                it
            }.test {
                delay(210)
                val result = expectMostRecentItem()
                Assert.assertEquals(result.payload, true)
                coVerify { dataSource.doLogin("email", "password") }
            }
        }
    }

    @Test
    fun testRegister() {
        coEvery { dataSource.doRegister("fullName", "email", "password") } returns true
        runTest {
            repository.doRegister("fullName", "email", "password").map {
                delay(100)
                it
            }.test {
                delay(210)
                val result = expectMostRecentItem()
                Assert.assertEquals(result.payload, true)
                coVerify { dataSource.doRegister("fullName", "email", "password") }
            }
        }
    }

    @Test
    fun testLoggedIn() {
        coEvery { dataSource.isLoggedIn() } returns true
        val result = repository.isLoggedIn()
        assertTrue(result)
        coVerify { dataSource.isLoggedIn() }
    }

    @Test
    fun testLogout() {
        coEvery { dataSource.doLogout() } returns true
        val result = repository.doLogout()
        assertTrue(result)
        coVerify { dataSource.doLogout() }
    }
}
