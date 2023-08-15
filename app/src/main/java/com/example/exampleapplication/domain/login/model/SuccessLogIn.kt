package com.example.exampleapplication.domain.login.model

import com.example.exampleapplication.domain.login.model.LogInState
import com.example.exampleapplication.presentation.fragments.login.LogInScreenModel

class SuccessLogIn(val token: String) : LogInState {
    override fun accept(loginScreen: LogInScreenModel) {
        loginScreen.successLogin()
    }
}
