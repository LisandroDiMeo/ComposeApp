package com.example.exampleapplication.domain.login.usecase

import com.example.exampleapplication.data.login.repository.ConcreteLogInRepository
import com.example.exampleapplication.data.login.repository.LogInRepository
import com.example.exampleapplication.domain.login.model.FailedLogIn
import com.example.exampleapplication.domain.login.model.IncorrectCredentials
import com.example.exampleapplication.domain.login.model.SuccessLogIn
import kotlinx.coroutines.flow.flow

class ConcreteLogInUseCase(
    private val repository: LogInRepository = ConcreteLogInRepository()
) : LogInUseCase {
    override fun logInWithCredentials(username: String, password: String) = flow {
        val response = repository.logInWithCredentials(
            username,
            password
        )
        if (response.isSuccessful) {
            response.body()?.let { logInResponse ->
                if (logInResponse.errorBody != null) emit(IncorrectCredentials())
                else emit(SuccessLogIn(logInResponse.token ?: ""))
            } ?: emit(FailedLogIn())
        } else {
            emit(FailedLogIn())
        }

    }
}
