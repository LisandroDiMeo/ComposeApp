package com.example.exampleapplication.presentation.fragments.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exampleapplication.domain.login.FailedLogIn
import com.example.exampleapplication.domain.login.LogInState
import com.example.exampleapplication.domain.login.SuccessLogIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.junit.internal.runners.statements.Fail
import kotlin.random.Random

class LogInViewModel : ViewModel() {
    private val _username: MutableStateFlow<String> = MutableStateFlow("")
    val username: StateFlow<String> = _username.asStateFlow()

    private val _password: MutableStateFlow<String> = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _logInAvailable: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val logInAvailable: StateFlow<Boolean> = _logInAvailable.asStateFlow()

    private val _credentialsResult: MutableStateFlow<LogInState?> = MutableStateFlow(null)
    val credentialsResult: StateFlow<LogInState?> = _credentialsResult.asStateFlow()

    private var logInJob: Job? = null

    fun updateUsername(username: String) {
        _username.value = username
        validate()
    }

    fun updatePassword(password: String) {
        _password.value = password
        validate()
    }

    private fun validate() {
        _logInAvailable.value = validatePassword() && validateUsername()
    }

    private fun validateUsername() = username.value.trim().length >= 8

    private fun validatePassword() = password.value.trim().length >= 8

    fun logIn() {
        logInJob?.cancel()
        _logInAvailable.value = false
        logInJob = viewModelScope.launch {
            withContext(Dispatchers.IO) {
                delay(3000L)
                if (username.value == "Lisandroid" && password.value == "12345678") {
                    _credentialsResult.value = SuccessLogIn()
                } else {
                    _credentialsResult.value = FailedLogIn()
                }
                _logInAvailable.value = true
            }
        }
    }

}
