package com.example.exampleapplication.domain.login.usecase

import com.example.exampleapplication.domain.login.model.LogInState
import kotlinx.coroutines.flow.Flow

interface LogInUseCase {
    fun logInWithCredentials(
        username: String,
        password: String
    ) : Flow<LogInState>
}
