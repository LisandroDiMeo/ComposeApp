package com.example.exampleapplication.data.login.repository

import com.example.exampleapplication.data.login.model.LogInResponse
import retrofit2.Response

interface LogInRepository {
    suspend fun logInWithCredentials(
        username: String,
        password: String
    ): Response<LogInResponse>
}
