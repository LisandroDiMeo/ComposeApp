package com.example.exampleapplication.data.login.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LogInResponse(
    @field:Json(name = "session-token")
    val token: String?,
    @field:Json(name = "error-body")
    val errorBody: String?
)
