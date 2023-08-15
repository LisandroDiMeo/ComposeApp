package com.example.exampleapplication.domain.login.model

import com.example.exampleapplication.presentation.fragments.login.LogInScreenModel

interface LogInState {
    fun accept(loginScreen: LogInScreenModel)
}

