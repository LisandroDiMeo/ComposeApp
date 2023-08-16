package com.example.exampleapplication.presentation.fragments.rotation

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
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
import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

@OptIn(ExperimentalTextApi::class, ExperimentalAnimationApi::class)
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
    var crossRadius by remember { mutableStateOf(1f) }
    var circleCenter by remember { mutableStateOf(Offset.Zero) }
    var dragStartedAngle by remember { mutableStateOf(0f) }
    var shouldAcceptDrag by remember { mutableStateOf(false) }
    var rotationAngle by remember { mutableStateOf(0f) }
    var oldAngle by remember { mutableStateOf(rotationAngle) }

    var dragEnded by remember { mutableStateOf(true) }
    val limits = (1..12).toList().map { it * 30 }

    LaunchedEffect(rotationAngle) {
        onAngleChanged(rotationAngle)
    }
    LaunchedEffect(dragEnded) {
        val transposedLimits = limits.map { transposeAngle(it) }
        if (dragEnded && rotationAngle.roundToInt() !in transposedLimits) {
            val closest = closestElement(transposedLimits, rotationAngle.roundToInt())
            // TODO: Refactor this!
            if (closest > rotationAngle){
                while (closest > rotationAngle.roundToInt()) {
                    rotationAngle += 1
                    delay(75L)
                }
            } else {
                while (closest < rotationAngle.roundToInt()) {
                    rotationAngle -= 1
                    delay(75L)
                }
            }

        }
    }

    var isVisible by remember { mutableStateOf(true) }

    AnimatedVisibility(
        visible = isVisible,
        enter = scaleIn(),
        exit = scaleOut()
    ) {
        Canvas(
            modifier = Modifier
                .size(512.dp)
                .pointerInput(true) {
                    detectTapGestures(onPress = { offset ->
                        if (isOnCircle(circleCenter, offset, crossRadius)) {
                            isVisible = false
                        }
                    })
                }
                .pointerInput(true) {
                    detectDragGestures(
                        onDragStart = { dragStart ->
                            dragEnded = false
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
                            dragEnded = true
                            if(transposeAngle(rotationAngle.roundToInt()) !in 0..180) {
                                // Logic to avoid going oustide preffered bounds. Here I selected from 0 to 180 degrees (transposed).
                                val distToLeft = abs(90 - rotationAngle)
                                val distToRight = abs(270 - rotationAngle)
                                rotationAngle = if (distToLeft < distToRight) {
                                    90f
                                } else {
                                    270f
                                }
                            }

                            if (shouldAcceptDrag)
                                oldAngle = rotationAngle
                        },
                        onDrag = { change, _ ->
                            if (shouldAcceptDrag && transposeAngle(rotationAngle.roundToInt()) in 0..180) {
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
                                Log.i("ANGLES", "Touch angle $touchAngle, rotation angle: $rotationAngle")
                            }
                        }
                    )
                }
        ) {

            // The Main Circle of the Goniometer, this radius may vary.
            val goniometerRadius = (size.width * .5f) - (.25f * (size.width * .5f))
            circleRadius = goniometerRadius
            circleCenter = size.center
            drawCircle(
                goniometerBrush,
                radius = goniometerRadius,
                center = size.center
            )

            // The cross that shrinks the Goniometer
            crossRadius = (size.width * .10f)
            drawCircle(
                Color(0xff1e4646),
                radius = crossRadius,
                center = size.center
            )
            // sin(45) = cos(45) = sqrt(2)/2
            val lineStartOffset = Offset(
                x = size.center.x + .75f * crossRadius * sqrt(2f)/2f,
                y = size.center.y + .75f * crossRadius * sqrt(2f)/2f
            )
            val lineEndOffset = Offset(
                x = size.center.x - .75f * crossRadius * sqrt(2f)/2f,
                y = size.center.y - .75f * crossRadius * sqrt(2f)/2f
            )
            val lineStartOffset2 = Offset(
                x = size.center.x + .75f * crossRadius * sqrt(2f)/2f,
                y = size.center.y + .75f * crossRadius * (-1) * sqrt(2f)/2f
            )
            val lineEndOffset2 = Offset(
                x = size.center.x - .75f * crossRadius * sqrt(2f)/2f,
                y = size.center.y - .75f * crossRadius * (-1) * sqrt(2f)/2f
            )
            drawLine(
                color = Color.Cyan,
                start = lineStartOffset,
                end = lineEndOffset,
                strokeWidth = 10f
            )
            drawLine(
                color = Color.Cyan,
                start = lineStartOffset2,
                end = lineEndOffset2,
                strokeWidth = 10f
            )

            // We only need to rotate the indicators, not the circle itself.
            // Also, we shift 180 degrees to the left
            // in order to have 90 degrees on top at start.
            rotate(rotationAngle - 180f, size.center) {
                // The Angles to select
                // Here we will use the polar system to mark each line
                val angleUnit = 30
                val subAngleUnit = 2
                val anglesSeparators = 360 / angleUnit
                val subAnglesSeparators = angleUnit / subAngleUnit
                for (marker in 1..anglesSeparators) {
                    val angleInDegrees = angleUnit * marker
                    // A certain constant is applied to
                    // the radius in this case to make the lines cut the circle
                    // diameter.
                    val angleInRadians = Math.toRadians(angleInDegrees.toDouble())
                    val cosineOfAngle = cos(
                        angleInRadians
                    ).toFloat()
                    val sineOfAngle = sin(
                        angleInRadians
                    ).toFloat()
                    val startOffset = Offset(
                        x = size.center.x + .90f * goniometerRadius * cosineOfAngle,
                        y = size.center.y + .90f * goniometerRadius * sineOfAngle
                    )
                    val endOffset = Offset(
                        x = size.center.x + 1.10f * goniometerRadius * cosineOfAngle,
                        y = size.center.y + 1.10f * goniometerRadius * sineOfAngle
                    )
                    drawLine(
                        color = Color.Gray,
                        start = startOffset,
                        end = endOffset,
                        strokeWidth = 10f
                    )
                    // For similar purposes, we apply a constant to
                    // the radius here to place the text above the marker.
                    val textOffset = Offset(
                        x = size.center.x + 1.25f * goniometerRadius * cosineOfAngle,
                        y = size.center.y + 1.25f * goniometerRadius * sineOfAngle
                    )
                    rotate(angleInDegrees.toFloat() + 90f, textOffset) {
                        drawText(
                            textMeasurer = textMeasurer,
                            text = "${angleInDegrees % 360}",
                            topLeft = textOffset
                        )
                    }

                    for (subMarker in 1..subAnglesSeparators) {
                        // For the sub-angle markers, we don't need need them
                        // to cut the circle diameter, only to touch it.
                        val subAngleInDegrees = angleInDegrees + subAngleUnit * subMarker
                        val subAngleInRadians = Math.toRadians(subAngleInDegrees.toDouble())
                        val cosineOfSubAngle = cos(
                            subAngleInRadians
                        ).toFloat()
                        val sineOfSubAngle = sin(
                            subAngleInRadians
                        ).toFloat()
                        val subStartOffset = Offset(
                            x = size.center.x + .90f * goniometerRadius * cosineOfSubAngle,
                            y = size.center.y + .90f * goniometerRadius * sineOfSubAngle
                        )
                        val subEndOffset = Offset(
                            x = size.center.x + goniometerRadius * cosineOfSubAngle,
                            y = size.center.y + goniometerRadius * sineOfSubAngle
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


}

@Preview(device = Devices.NEXUS_5X)
@Composable
fun RotationPreview() {
    var angle by remember { mutableStateOf("") }
    var rotationAngle by remember { mutableStateOf(90f) }
    Column(Modifier.fillMaxSize()) {
        Text(angle)
        ImageRotation {
            val absoluteAngle = (90 - it.roundToInt()) % 360
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

private fun transposeAngle(angle: Int): Int {
    val x = (90 - angle) % 360
    return if (x < 0) x + 360 else x
}

fun closestElement(l: List<Int>, k: Int): Int{
    var closestElement = l[0]
    var closestDifference = abs(l[0] - k)

    for (element in l) {
        val difference = abs(element - k)
        if (difference < closestDifference) {
            closestDifference = difference
            closestElement = element
        }
    }
    return closestElement
}

