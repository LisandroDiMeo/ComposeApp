package com.example.exampleapplication.presentation.fragments.rotation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import kotlin.math.cos
import kotlin.math.sin

@Preview
@Composable
fun ExampleCircle() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawCircle(
            color = Color.Cyan,
            center = size.center,
            radius = 200f
        )
        val angleInDegrees = 330
        val angleInRadians = Math.toRadians(angleInDegrees.toDouble())
        val cosineOfAngle = cos(
            angleInRadians
        ).toFloat()
        val sineOfAngle = sin(
            angleInRadians
        ).toFloat()
        val startOffset = Offset(
            x = size.center.x + .90f * 200f * cosineOfAngle,
            y = size.center.y - .90f * 200f * sineOfAngle
        )
        val endOffset = Offset(
            x = size.center.x + 1.10f * 200f * cosineOfAngle,
            y = size.center.y - 1.10f * 200f * sineOfAngle
        )
        drawLine(
            color = Color.Red,
            start = startOffset,
            end = endOffset,
            strokeWidth = 10f
        )
    }
}
