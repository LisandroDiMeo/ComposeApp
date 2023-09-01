package com.example.exampleapplication.presentation.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.common.ui.topappbar.TopBar
import com.example.exampleapplication.presentation.fragments.login.LogInScreen
import com.example.exampleapplication.presentation.fragments.login.LogInScreenActions
import com.example.exampleapplication.presentation.fragments.login.LogInScreenModel

@Preview
@Composable
fun MainNavGraph() {
    val navController = rememberNavController()
    val mainNavViewModel = viewModel(
        modelClass = MainNavigationViewModel::class.java,
    )
    mainNavViewModel.screen.collectAsStateWithLifecycle().value?.let { screenUi ->
        TopBar(
            title = screenUi.topBarTitle,
            actions = screenUi.topAppBarActions,
            navAction = screenUi.navigationAction,
            titleStyle = screenUi.titleStyle
        )
    }
    NavHost(
        navController = navController,
        route = MainNavRoutes.LogInRoutes.Route,
        startDestination = MainNavRoutes.LogInRoutes.PromptCredentials
    ) {
        composable(route = MainNavRoutes.LogInRoutes.PromptCredentials) {
            val screen = remember {
                LogInScreenModel(
                    LogInScreenActions(
                        onInformationClicked = {}, onSettingsClicked = {},
                        onIncorrectCredentials = {
                            navController.navigate(MainNavRoutes.LogInRoutes.RememberCredentials)
                        },
                        onSuccessLogIn = {
                            navController.navigate(MainNavRoutes.LogInRoutes.Home) {
                                popUpTo(MainNavRoutes.LogInRoutes.PromptCredentials) {
                                    inclusive = true
                                }
                            }
                        }
                    )
                )
            }
            LaunchedEffect(screen) {
                mainNavViewModel.updateTopBarWithScreen(screen)
            }
            LogInScreen(screen)
        }

        composable(route = MainNavRoutes.LogInRoutes.RememberCredentials) {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(text = "Remember credentials :(")
            }
        }

        composable(route = MainNavRoutes.LogInRoutes.Home) {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(text = "Home, sweet home.")
            }
        }
    }
}

