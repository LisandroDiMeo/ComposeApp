package com.example.common.ui.topappbar

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun TopBar(
    title: AnnotatedString,
    actions: List<TopAppBarAction>,
    navAction: TopAppBarAction?,
    titleStyle: TextStyle
) {
    TopAppBar(
        elevation = 4.dp,
        title = {
            Text(title, style = titleStyle)
        },
        backgroundColor = MaterialTheme.colorScheme.background,
        navigationIcon = {
            navAction?.let {
                IconButton(onClick = navAction.onClick) {
                    Icon(navAction.icon, navAction.contentDescription)
                }
            }
        }, actions = {
            actions.forEach {
                IconButton(onClick = it.onClick) {
                    Icon(it.icon, it.contentDescription)
                }
            }
        })
}

data class TopAppBarAction(
    val icon: ImageVector,
    val contentDescription: String?,
    val onClick: () -> Unit = {}
)
