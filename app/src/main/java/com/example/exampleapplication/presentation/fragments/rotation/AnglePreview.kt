package com.example.exampleapplication.presentation.fragments.rotation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun AnglePreview(
    modifier: Modifier = Modifier,
    angle: String = "160°"
) {
    Box(modifier = modifier
        .size(64.dp)
        .background(Color(0xff30302f), shape = CircleShape)) {
        Text(
            text = angle,
            color = Color.White,
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
