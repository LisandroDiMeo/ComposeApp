package com.example.exampleapplication.data.login.repository

import com.example.exampleapplication.data.login.LogInService
import com.example.exampleapplication.data.login.model.LogInRequest
import com.example.exampleapplication.data.login.model.LogInResponse
import retrofit2.Response

class ConcreteLogInRepository : LogInRepository {
    private val service = LogInService.instance

    override suspend fun logInWithCredentials(
        username: String,
        password: String
    ): Response<LogInResponse> {
        return service.logInWithCredentials(LogInRequest(username, password))
    }
}
