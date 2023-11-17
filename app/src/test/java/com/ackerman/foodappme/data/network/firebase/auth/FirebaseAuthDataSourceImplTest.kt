package com.ackerman.foodappme.data.network.firebase.auth

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import io.mockk.MockKAnnotations.init
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FirebaseAuthDataSourceImplTest {
    @MockK(relaxed = true)
    lateinit var firebaseAuth: FirebaseAuth
    private lateinit var dataSource: FirebaseAuthDataSource

    @Before
    fun setUp() {
        init(this)
        dataSource = FirebaseAuthDataSourceImpl(firebaseAuth)
    }

    @Test
    fun getCurrentUser() {
        val user = mockk<FirebaseUser>()
        every { firebaseAuth.currentUser } returns user
        val result = dataSource.getCurrentUser()
        verify { firebaseAuth.currentUser }
        assertEquals(user, result)
    }

    @Test
    fun testLogin() {
        every { firebaseAuth.signInWithEmailAndPassword(any(), any()) } returns mockTaskAuthResult()
        runTest {
            val result = dataSource.doLogin("email", "password")
            assertEquals(result, true)
            verify { firebaseAuth.signInWithEmailAndPassword(any(), any()) }
        }
    }

    private fun mockTaskAuthResult(exception: Exception? = null): Task<AuthResult> {
        val task: Task<AuthResult> = mockk(relaxed = true)
        every { task.isComplete } returns true
        every { task.exception } returns exception
        every { task.isCanceled } returns false
        val relaxedResult: AuthResult = mockk(relaxed = true)
        every { task.result } returns relaxedResult
        every { task.result.user } returns mockk(
            relaxed = true
        )
        return task
    }

    @Test
    fun testRegister() {
        runTest {
            mockkConstructor(UserProfileChangeRequest.Builder::class)
            every { anyConstructed<UserProfileChangeRequest.Builder>().build() } returns mockk()

            val mockAuthResult = mockTaskAuthResult()
            every {
                firebaseAuth.createUserWithEmailAndPassword(
                    any(),
                    any()
                )
            } returns mockAuthResult

            val mockUser = mockk<FirebaseUser>(relaxed = true)
            every { mockAuthResult.result.user } returns mockUser
            every { mockUser.updateProfile(any()) } returns mockTaskVoid()

            val result = dataSource.doRegister("name", "email", "password")
            assertEquals(result, true)
        }
    }

    private fun mockTaskVoid(exception: Exception? = null): Task<Void> {
        val task: Task<Void> = mockk(relaxed = true)
        every { task.isComplete } returns true
        every { task.exception } returns exception
        every { task.isCanceled } returns false
        val relaxedVoid: Void = mockk(relaxed = true)
        every { task.result } returns relaxedVoid
        return task
    }

    @Test
    fun `test isLoggedIn, user not null`() {
        val user = mockk<FirebaseUser>()
        every { firebaseAuth.currentUser } returns user
        val result = dataSource.getCurrentUser()
        verify { firebaseAuth.currentUser }
        assertTrue(result != null)
    }

    @Test
    fun testLogout() {
        mockkStatic(FirebaseAuth::class)
        every { FirebaseAuth.getInstance() } returns firebaseAuth
        every { firebaseAuth.signOut() } returns Unit
        val result = dataSource.doLogout()
        assertEquals(result, true)
    }
}
