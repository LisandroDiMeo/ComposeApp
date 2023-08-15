package com.example.exampleapplication.domain.login.model

import com.example.exampleapplication.domain.login.model.LogInState
import com.example.exampleapplication.presentation.fragments.login.LogInScreenModel

class FailedLogIn: LogInState {
    override fun accept(loginScreen: LogInScreenModel) {
        loginScreen.failedLogIn()
    }
}
