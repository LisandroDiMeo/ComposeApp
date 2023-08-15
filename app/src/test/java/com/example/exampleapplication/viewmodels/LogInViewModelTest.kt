package com.example.exampleapplication.viewmodels

import com.example.exampleapplication.domain.login.model.FailedLogIn
import com.example.exampleapplication.domain.login.model.IncorrectCredentials
import com.example.exampleapplication.domain.login.model.LogInState
import com.example.exampleapplication.domain.login.model.SuccessLogIn
import com.example.exampleapplication.domain.login.usecase.LogInUseCase
import com.example.exampleapplication.presentation.fragments.login.LogInViewModel
import com.example.test.utils.className
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class LogInViewModelTest {

    private val logInUseCase = FakeLogInUseCase()

    @Test
    fun `LogIn ViewModel starts with disabled log in, empty username & password, and no credential result`() {
        val viewModel = LogInViewModel(
            logInUseCase
        )
        assertEquals("", viewModel.password.value)
        assertEquals("", viewModel.username.value)
        assertEquals(false, viewModel.logInAvailable.value)
        assertEquals(null, viewModel.credentialsResult.value)
    }

    @Test
    fun `LogIn ViewModel has disabled log in if username and password does not met criteria`() {
        val viewModel = LogInViewModel(
            logInUseCase
        )
        // Username Not Compliant
        viewModel.updateUsername("user")
        viewModel.updatePassword("password")
        assertEquals(false, viewModel.logInAvailable.value)
        // Password Not Compliant
        viewModel.updatePassword("pass")
        viewModel.updateUsername("username")
        assertEquals(false, viewModel.logInAvailable.value)
        // Both Not compliant
        viewModel.updatePassword("pass")
        viewModel.updateUsername("user")
        assertEquals(false, viewModel.logInAvailable.value)
    }

    @Test
    fun `LogIn ViewModel has enabled log in if both username and password are compliant`() {
        val viewModel = LogInViewModel(
            logInUseCase
        )
        viewModel.updatePassword("passname")
        viewModel.updateUsername("userword")
        assertEquals(true, viewModel.logInAvailable.value)
    }

    @Test
    fun `LogIn ViewModel throws an Exception if logIn is done while logIn is disabled`() {
        val viewModel = LogInViewModel(
            logInUseCase
        )
        val exception = assertThrows(IllegalStateException::class.java) {
            viewModel.logIn()
        }
        assertEquals(LogInViewModel.ILLEGAL_LOGIN, exception.message)
    }

    @Test
    fun `LogIn ViewModel throws an Exception if logIn is done while logIn job is running`() {
        val viewModel = LogInViewModel(
            logInUseCase
        )
        logInUseCase.nextState = FailedLogIn()
        viewModel.apply {
            updateUsername("username")
            updatePassword("password")
            val exception = assertThrows(IllegalStateException::class.java) {
                logIn()
                logIn()
            }
            assertEquals(LogInViewModel.ILLEGAL_LOGIN, exception.message)
        }
    }

    @Test
    fun `LogIn ViewModel performs logIn and return a failed state if credentials are incorrect`() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val viewModel = LogInViewModel(
            dispatcher = testDispatcher,
            logInUseCase = logInUseCase
        )
        logInUseCase.nextState = IncorrectCredentials()
        viewModel.apply {
            updateUsername("username")
            updatePassword("password")
            logIn()
            advanceUntilIdle()
            assertEquals(
                className(IncorrectCredentials::class.java),
                className(viewModel.credentialsResult.value?.javaClass)
            )
        }
    }

    @Test
    fun `LogIn ViewModel performs logIn and returns a failed state if a network error occur`() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val viewModel = LogInViewModel(
            dispatcher = testDispatcher,
            logInUseCase = logInUseCase
        )
        logInUseCase.nextState = FailedLogIn()
        viewModel.apply {
            updateUsername("username")
            updatePassword("password")
            logIn()
            advanceUntilIdle()
            assertEquals(
                className(FailedLogIn::class.java),
                className(viewModel.credentialsResult.value?.javaClass)
            )
        }
    }

    @Test
    fun `LogIn ViewModel performs logIn and returns a success state with a token in case of valid credentials`() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val viewModel = LogInViewModel(
            dispatcher = testDispatcher,
            logInUseCase = logInUseCase
        )
        val token = "fake-token"
        logInUseCase.nextState = SuccessLogIn(token)
        viewModel.apply {
            updateUsername("username")
            updatePassword("password")
            logIn()
            advanceUntilIdle()
            assertEquals(
                className(SuccessLogIn::class.java),
                className(viewModel.credentialsResult.value?.javaClass)
            )
            assertEquals(
                token,
                (viewModel.credentialsResult.value as? SuccessLogIn)?.token
            )
        }

    }

    class FakeLogInUseCase : LogInUseCase {
        var nextState: LogInState? = null
        override fun logInWithCredentials(
            username: String,
            password: String
        ): Flow<LogInState> = flow { nextState?.let { emit(it) } }
    }
}


