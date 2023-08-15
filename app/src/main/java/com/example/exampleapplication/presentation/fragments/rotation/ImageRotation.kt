package com.example.exampleapplication.presentation.fragments.rotation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextLayoutInput
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.style.TextGeometricTransform
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.exampleapplication.R
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalTextApi::class)
@Preview(device = Devices.PIXEL_3A)
@Composable
fun ImageRotation() {
    val goniometerBgColor = Color(0x4a00FFFF)
    val textMeasurer = rememberTextMeasurer()
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(true) {
                detectDragGestures(
                    onDragStart = { dragStart ->

                    },
                    onDrag = { change, dragAmount ->

                    }
                )
            }
    ) {

        // The Main Circle of the Goniometer
        val goniometerRadius = (size.width * .5f) - (.25f * (size.width * .5f))
        drawCircle(
            color = goniometerBgColor,
            radius = goniometerRadius,
            center = size.center
        )

        // The Angles to select
        // Here we will use the polar system to mark each line
        val angleUnit = 30
        val subAngleUnit = 2
        val anglesSeparators = 360 / angleUnit
        val subAnglesSeparators = angleUnit / subAngleUnit
        for (marker in 1..anglesSeparators) {
            val angleInDegrees = angleUnit * marker
            val startOffset = Offset(
                x = size.center.x + .90f * goniometerRadius * cos(
                    Math.toRadians(angleInDegrees.toDouble())
                ).toFloat(),
                y = size.center.y + .90f * goniometerRadius * sin(
                    Math.toRadians(angleInDegrees.toDouble())
                ).toFloat()
            )
            val endOffset = Offset(
                x = size.center.x + 1.10f * goniometerRadius * cos(
                    Math.toRadians(angleInDegrees.toDouble())
                ).toFloat(),
                y = size.center.y + 1.10f * goniometerRadius * sin(
                    Math.toRadians(angleInDegrees.toDouble())
                ).toFloat()
            )
            drawLine(
                color = Color.Gray,
                start = startOffset,
                end = endOffset,
                strokeWidth = 10f
            )
            val textOffset = Offset(
                x = size.center.x + 1.25f * goniometerRadius * cos(
                    Math.toRadians(angleInDegrees.toDouble())
                ).toFloat(),
                y = size.center.y + 1.25f * goniometerRadius * sin(
                    Math.toRadians(angleInDegrees.toDouble())
                ).toFloat()
            )
            rotate(angleInDegrees.toFloat() + 90f, textOffset) {
                drawText(
                    textMeasurer = textMeasurer,
                    text = "$angleInDegrees",
                    topLeft = textOffset
                )
            }


            for (subMarker in 1..subAnglesSeparators) {
                val subAngleInDegrees = angleInDegrees + subAngleUnit * subMarker
                val subStartOffset = Offset(
                    x = size.center.x + .90f * goniometerRadius * cos(
                        Math.toRadians(subAngleInDegrees.toDouble())
                    ).toFloat(),
                    y = size.center.y + .90f * goniometerRadius * sin(
                        Math.toRadians(subAngleInDegrees.toDouble())
                    ).toFloat()
                )
                val subEndOffset = Offset(
                    x = size.center.x + goniometerRadius * cos(
                        Math.toRadians(subAngleInDegrees.toDouble())
                    ).toFloat(),
                    y = size.center.y + goniometerRadius * sin(
                        Math.toRadians(subAngleInDegrees.toDouble())
                    ).toFloat()
                )
                drawLine(
                    color = Color.Gray,
                    start = subStartOffset,
                    end = subEndOffset,
                    strokeWidth = 5f
                )
            }
        }
    }

}
