package com.example.exampleapplication.presentation.fragments.rotation

import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.center
import androidx.compose.ui.unit.dp
import java.lang.Math.atan2
import java.text.DecimalFormat
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.pow

private fun Float.toRad(): Float {
    return this * (Math.PI / 180f).toFloat()
}

private fun isOnCircle(p1: Offset, p2: Offset, radius: Float): Boolean {
    return sqrt(
        abs((p1.x - p2.x)).pow(2) + abs((p1.y - p2.y)).pow(2)
    ) <= radius
}

@Preview(device = Devices.NEXUS_5X)
@Composable
fun VolumeKnob(
    modifier: Modifier = Modifier.fillMaxSize(),
    numberOfCircles: Int = 40,
    minSize: Dp = 64.dp,
    onVolumeChanged: (Float) -> Unit = {
        Log.i("ANGLE", it.toString())
    }
) {

    var angle by remember { mutableStateOf(0f) }
    var dragStartedAngle by remember { mutableStateOf(0f) }
    var oldAngle by remember { mutableStateOf(angle) }
    var volume by remember { mutableStateOf(0f) }
    var circleOrigin by remember { mutableStateOf(Offset(0F,0F)) }
    var circleRadius by remember { mutableStateOf(0F) }

    //represents the nob state ON or OFF
    var state by remember { mutableStateOf(false) }
    val colourTransitionDurationMs = 1000

    val volumeFormatter = DecimalFormat("#.#")
    val ringColorOff = Color.LightGray
    val ringColorON = Color.Green
    val dotCircleNormalColor = Color(0xff00ff00)
    val dotCircleEmphasisedColor = Color(0xff9737bf)
    val offsetAngleDegree = 20f

    val ringColor by animateColorAsState(
        targetValue = if (state) ringColorON else ringColorOff,
        animationSpec = tween(
            durationMillis = colourTransitionDurationMs,
        )
    )

    BoxWithConstraints() {

        val width = if (minWidth < 1.dp)
            minSize else minWidth

        val height = if (minHeight < 1.dp)
            minSize else minHeight

        Canvas(
            modifier = modifier
                .size(width, height)
                .pointerInput(true) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            if (isOnCircle(offset, circleOrigin, circleRadius)) {
                                dragStartedAngle = atan2(
                                    y = size.center.x - offset.x,
                                    x = size.center.y - offset.y
                                ) * (180f / Math.PI.toFloat()) * -1
                            }
                        },
                        onDragEnd = {
                            oldAngle = angle
                        }
                    ) { change, offset ->
                        val touchAngle = atan2(
                            y = size.center.x - change.position.x,
                            x = size.center.y - change.position.y
                        ) * (180f / Math.PI.toFloat()) * -1

                        angle = oldAngle + (touchAngle - dragStartedAngle)

                        //we want to work with positive angles
                        if (angle > 360) {
                            angle -= 360
                        } else if (angle < 0) {
                            angle = 360 - abs(angle)
                        }

                        if (angle > 360f - (offsetAngleDegree * .8f))
                            angle = 0f
                        else if (angle > 0f && angle < offsetAngleDegree)
                            angle = offsetAngleDegree
                        //determinants the state of the nob. OFF or ON
                        state =
                            angle >= offsetAngleDegree && angle <= (360f - offsetAngleDegree / 2)

                        val newVolume = if (angle < offsetAngleDegree)
                            0f
                        else
                            (angle) / (360f - offsetAngleDegree)

                        volume = newVolume.coerceIn(
                            minimumValue = 0f,
                            maximumValue = 1f
                        )

                        onVolumeChanged(newVolume)
                    }
                }
        ) {
            //just calculating the radius so that the circle will fill the parent
            //also adding 25% padding of the radius as a padding to the parent..
            val radius = (size.width * .5f) - (.25f * (size.width * .5f))

            drawCircle(
                color = ringColor,
                style = Stroke(
                    width = (radius * .1f)
                ),
                radius = radius,
                center = size.center
            )

            // Represents the angle difference between each dot.
            // We are taking into consideration the offsetAngleDegree as wall.
            val lineDegree = (360f - offsetAngleDegree * 2) / numberOfCircles

            for (circleNumber in 0..numberOfCircles) {


                val angleInDegrees = lineDegree * circleNumber - 90f + offsetAngleDegree
                val angleRad = Math.toRadians(angleInDegrees.toDouble()).toFloat()

                val isDotEmphasised =
                    angle >= angleInDegrees + 90f && angle < (360f - offsetAngleDegree / 2)

                val normalDotRad = radius * .015f
                val emphasisedDotRad = normalDotRad + (circleNumber * (radius * .001f))

                val dotRadius = if (isDotEmphasised)
                    emphasisedDotRad
                else normalDotRad

                val dotColor = if (isDotEmphasised)
                    dotCircleEmphasisedColor
                else dotCircleNormalColor

                //basically we are shifting the dot in both X and Y directions by this amount
                val dotDistanceFromMainCircle = radius * .15f

                drawCircle(
                    center = Offset(
                        x = (radius + dotDistanceFromMainCircle) * cos(angleRad) + size.center.x,
                        y = (radius + dotDistanceFromMainCircle) * sin(angleRad) + size.center.y
                    ),
                    color = dotColor,
                    radius = dotRadius
                )
            }

            val spacing = radius * .225f // ~22% of the radius

            val knobCenter = Offset(
                x = (radius - spacing) * cos((angle - 90f).toRad()) + size.center.x,
                y = (radius - spacing) * sin((angle - 90f).toRad()) + size.center.y
            )
            circleOrigin = knobCenter
            circleRadius = radius * (1f / 9f)
            //the nob itself
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color.Transparent, Color.Gray),
                    center = knobCenter,
                    radius = radius * (1f / 9f)
                ),
                radius = radius * (1f / 9f),
                center = knobCenter
            )

            //state text.
            drawContext.canvas.nativeCanvas.apply {
                val text = if (state) "ON" else "OFF"
                val paint = Paint()
                paint.textSize = radius * .15f
                val textRect = Rect()
                paint.getTextBounds(text, 0, text.length, textRect)

                val positionX = size.center.x - (textRect.width() / 2)
                val positionY = size.center.y - radius - (.15f * radius) + (textRect.height() / 2)

                drawText(
                    text,
                    positionX,
                    positionY,
                    paint
                )

                if (state) {
                    val volumeText = if (state) {
                        volumeFormatter.format((volume * 100))
                    } else "OFF"

                    val volumePaint = Paint()
                    volumePaint.color = android.graphics.Color.parseColor("#9737bf")
                    volumePaint.textSize = (radius / 2)
                    val volumeTextRect = Rect()
                    volumePaint.getTextBounds(volumeText, 0, volumeText.length, volumeTextRect)

                    val volumeTextPositionX = size.center.x - (volumeTextRect.width() / 2)
                    val volumeTextPositionY = size.center.y + (volumeTextRect.height() / 2)

                    drawText(
                        volumeText,
                        volumeTextPositionX,
                        volumeTextPositionY,
                        volumePaint
                    )
                }


            }
        }
    }

}
