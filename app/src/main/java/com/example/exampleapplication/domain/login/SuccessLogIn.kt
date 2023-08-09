package com.example.exampleapplication.domain.login

import com.example.exampleapplication.presentation.fragments.login.LogInScreenModel

class SuccessLogIn : LogInState {
    override fun accept(loginScreen: LogInScreenModel) {
        loginScreen.successLogin()
    }
}
