package com.example.exampleapplication.presentation.fragments.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Preview(device = Devices.NEXUS_5X, showSystemUi = true)
@Composable
fun LogInScreen(
    logInScreenModel: LogInScreenModel = LogInScreenModel(LogInScreenActions()),
) {
    val viewModel = viewModel(
        modelClass = LogInViewModel::class.java
    )
    val username = viewModel.username.collectAsStateWithLifecycle().value
    val password = viewModel.password.collectAsStateWithLifecycle().value

    val loginState = viewModel.credentialsResult.collectAsStateWithLifecycle().value

    LaunchedEffect(loginState) {
        loginState?.accept(logInScreenModel)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = username,
            onValueChange = {
                viewModel.updateUsername(it)
            },
            leadingIcon = {
                Icon(imageVector = Icons.Filled.Person, null)
            },
            label = {
                Text(text = "Username")
            }
        )
        TextField(
            value = password,
            onValueChange = {
                viewModel.updatePassword(it)
            },
            leadingIcon = {
                Icon(imageVector = Icons.Filled.Lock, null)
            },
            label = {
                Text(text = "Password")
            }
        )
        Button(
            onClick = { viewModel.logIn() },
            modifier = Modifier,
            enabled = viewModel.logInAvailable.collectAsStateWithLifecycle().value,
            contentPadding = PaddingValues(horizontal = 32.dp, vertical = 18.dp),
        ) {
            Icon(imageVector = Icons.Filled.Star, null)
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = "Log In", color = Color.White)
        }
    }


}
