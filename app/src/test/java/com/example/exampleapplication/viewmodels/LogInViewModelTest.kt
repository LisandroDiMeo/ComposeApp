package com.example.exampleapplication.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.exampleapplication.presentation.fragments.login.LogInViewModel
import com.example.test.utils.MainCoroutineTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class LogInViewModelTest {
    @get:Rule
    val testRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineTestRule()

    @Test
    fun `LogIn ViewModel starts with disabled log in, empty username & password, and no credential result`() {
        val viewModel = LogInViewModel()
        assertEquals("", viewModel.password.value)
        assertEquals("", viewModel.username.value)
        assertEquals(false, viewModel.logInAvailable.value)
        assertEquals(null, viewModel.credentialsResult.value)
    }
    
}
