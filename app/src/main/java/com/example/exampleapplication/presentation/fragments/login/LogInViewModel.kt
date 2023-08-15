package com.example.exampleapplication.presentation.fragments.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exampleapplication.domain.login.model.LogInState
import com.example.exampleapplication.domain.login.usecase.ConcreteLogInUseCase
import com.example.exampleapplication.domain.login.usecase.LogInUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LogInViewModel(
    private val logInUseCase: LogInUseCase = ConcreteLogInUseCase(),
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
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
        if (!logInAvailable.value || logInJob?.isActive == true)
            throw IllegalStateException(ILLEGAL_LOGIN)
        logInJob?.cancel()
        _logInAvailable.value = false
        logInJob = viewModelScope.launch(dispatcher) {
            logInUseCase
                .logInWithCredentials(username.value, password.value)
                .collect {
                    _credentialsResult.value = it
                    _logInAvailable.value = true
                }
        }
    }

    companion object {
        const val ILLEGAL_LOGIN = "Its prohibited to perform log in with current set of credentials"
    }

}
