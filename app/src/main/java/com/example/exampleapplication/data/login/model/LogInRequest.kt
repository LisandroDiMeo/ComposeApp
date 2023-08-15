package com.example.exampleapplication.data.login.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LogInRequest(
    @field:Json(name = "username")
    val username: String,
    @field:Json(name = "password")
    val password: String
)
