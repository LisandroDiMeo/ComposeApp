package com.example.common.ui.topappbar

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle

interface ScreenModel {
    val topBarTitle: AnnotatedString
    val topAppBarActions: List<TopAppBarAction>
    val navigationAction: TopAppBarAction?
    val titleStyle: TextStyle
}
