package com.example.exampleapplication.presentation.fragments.rotation

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.center
import androidx.compose.ui.unit.dp
import com.example.exampleapplication.R
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

@OptIn(ExperimentalTextApi::class)
@Preview(device = Devices.PIXEL_3A)
@Composable
fun ImageRotation(
    onAngleChanged: (Float) -> Unit = {
        Log.i("IMAGE_ROTATE", it.toString())
    }
) {
    val goniometerBgColorStart = Color(0x4a0A1821)
    val goniometerBgColorEnd = Color(0xff4A8492)
    val goniometerBrush = Brush.horizontalGradient(
        listOf(goniometerBgColorStart, goniometerBgColorEnd)
    )
    val textMeasurer = rememberTextMeasurer()
    var circleRadius by remember { mutableStateOf(1f) }
    var circleCenter by remember { mutableStateOf(Offset.Zero) }
    var dragStartedAngle by remember { mutableStateOf(0f) }
    var shouldAcceptDrag by remember { mutableStateOf(false) }
    var rotationAngle by remember { mutableStateOf(0f) }
    var oldAngle by remember { mutableStateOf(rotationAngle) }

    LaunchedEffect(rotationAngle) {
        onAngleChanged(rotationAngle)
    }

    Canvas(
        modifier = Modifier
            .size(512.dp)
            .pointerInput(true) {
                detectDragGestures(
                    onDragStart = { dragStart ->
                        shouldAcceptDrag = isOnCircle(
                            circleCenter,
                            dragStart,
                            circleRadius
                        )
                        dragStartedAngle = atan2(
                            y = size.center.x - dragStart.x,
                            x = size.center.y - dragStart.y
                        ) * (180f / Math.PI.toFloat()) * -1
                    },
                    onDragEnd = {
                        if (shouldAcceptDrag)
                            oldAngle = rotationAngle
                    },
                    onDrag = { change, _ ->
                        if (shouldAcceptDrag) {
                            val touchAngle = atan2(
                                y = size.center.x - change.position.x,
                                x = size.center.y - change.position.y
                            ) * (180f / Math.PI.toFloat()) * -1
                            rotationAngle = oldAngle + (touchAngle - dragStartedAngle)
                            if (rotationAngle > 360) {
                                rotationAngle -= 360
                            } else if (rotationAngle < 0) {
                                rotationAngle = 360 - abs(rotationAngle)
                            }
                        }
                    }
                )
            }
    ) {

        // The Main Circle of the Goniometer
        val goniometerRadius = (size.width * .5f) - (.25f * (size.width * .5f))
        circleRadius = goniometerRadius
        circleCenter = size.center
        drawCircle(
            goniometerBrush,
            radius = goniometerRadius,
            center = size.center
        )

        // Temporary Angle Indicator
        rotate(rotationAngle, size.center) {
//            drawCircle(
//                color = Color.Red,
//                radius = goniometerRadius / 20,
//                center = Offset(x = center.x, y = center.y - (500f))
//            )
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
                        text = "${angleInDegrees % 360}",
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

}

@Preview(device = Devices.NEXUS_5X)
@Composable
fun RotationPreview() {
    var angle by remember { mutableStateOf("") }
    var rotationAngle by remember { mutableStateOf(0f) }
    Column(Modifier.fillMaxSize()) {
        Text(angle)
        ImageRotation {
            val absoluteAngle = (270 - it.roundToInt()) % 360
            val finalAngle = if (absoluteAngle < 0) (absoluteAngle + 360) % 360 else absoluteAngle
            rotationAngle = finalAngle.toFloat()
            angle = finalAngle.toString() + "º (${it.roundToInt()})"
        }
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier.rotate(rotationAngle),
            colorFilter = ColorFilter.tint(Color.Red)
        )
    }
}

private fun isOnCircle(circleCenter: Offset, point: Offset, radius: Float): Boolean {
    val isOnCircle = sqrt(
        abs((circleCenter.x - point.x)).pow(2) + abs((circleCenter.y - point.y)).pow(2)
    ) <= radius
    Log.i("MATH", "Is $point on circle with center $circleCenter and r=$radius? $isOnCircle")
    return isOnCircle
}
