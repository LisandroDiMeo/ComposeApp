package com.example.exampleapplication.data.login

import com.example.exampleapplication.BuildConfig
import com.example.exampleapplication.data.login.model.LogInRequest
import com.example.exampleapplication.data.login.model.LogInResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface LogInService {
    @POST("/login")
    suspend fun logInWithCredentials(
        @Body logInRequest: LogInRequest
    ): Response<LogInResponse>

    companion object {
        val instance: LogInService by lazy {
            Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .client(httpClient)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(LogInService::class.java)
        }

        private val httpLoggingInterceptor = {
            HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)
        }

        private val httpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor())
            .build()


    }
}


