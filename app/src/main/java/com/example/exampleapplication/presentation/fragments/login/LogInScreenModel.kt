package com.example.exampleapplication.presentation.fragments.login

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.example.common.ui.topappbar.TopAppBarAction
import com.example.common.ui.topappbar.ScreenModel
import com.example.exampleapplication.domain.login.SuccessLogIn

class LogInScreenModel(
    private val actions: LogInScreenActions,
) : ScreenModel {

    override val topBarTitle: AnnotatedString =
        buildAnnotatedString {
            withStyle(
                SpanStyle(
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
            ) {
                append("Log In")
            }
        }

    override val navigationAction: TopAppBarAction? = null

    override val topAppBarActions: List<TopAppBarAction> = listOf(
        TopAppBarAction(
            Icons.Filled.Info,
            "Application Information",
            actions.onInformationClicked
        ),
        TopAppBarAction(
            Icons.Filled.Settings,
            "Application Settings",
            actions.onSettingsClicked
        )
    )

    override val titleStyle: TextStyle = TextStyle(textAlign = TextAlign.Center)

    fun successLogin() {
        actions.onSuccessLogIn()
    }

    fun failureLogin() {
        actions.onFailedLogIn()
    }

}

data class LogInScreenActions(
    val onInformationClicked: () -> Unit = {},
    val onSettingsClicked: () -> Unit = {},
    val onSuccessLogIn: () -> Unit = {},
    val onFailedLogIn: () -> Unit = {}
)
