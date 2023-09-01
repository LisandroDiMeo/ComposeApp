package com.example.exampleapplication.domain.login.model

import com.example.exampleapplication.presentation.fragments.login.LogInScreenModel

class IncorrectCredentials : LogInState {
    override fun accept(loginScreen: LogInScreenModel) {
        loginScreen.incorrectCredentials()
    }
}
