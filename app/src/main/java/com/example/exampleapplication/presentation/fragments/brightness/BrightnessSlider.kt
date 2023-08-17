package com.example.exampleapplication.presentation.fragments.brightness

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SliderPositions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.lerp
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    device = Devices.NEXUS_5X,
    backgroundColor = 0x000000,
)
@Composable
fun BrightnessSelector(
    modifier: Modifier = Modifier,
    brightness: Float = .5f,
    onBrightnessChanged: (Float) -> Unit = {}
) {
//    var brightness by remember { mutableStateOf(.5f) }
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    LaunchedEffect(brightness) {
        onBrightnessChanged(brightness)
    }
//    Column(
//        modifier = Modifier
//            .background(Color.Black)
//            .fillMaxSize()
//            .padding(16.dp)
//            .pointerInput(true) {
//                detectHorizontalDragGestures { change, _ ->
//                    brightness = change.position.x / screenWidth.toPx()
//                }
//            }
//    ) {
//
//    }
    Slider(
        value = brightness,
        onValueChange = {
            onBrightnessChanged(it)
        },
        modifier = modifier,
        colors = SliderDefaults.colors(
            thumbColor = Color.Red,
        ),
        track = {
            CustomTrack(sliderPositions = it)
        },
        thumb = {
            Icon(
                Icons.Default.Star,
                contentDescription = "Change slider value",
                tint = Color.White
            )
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTrack(
    sliderPositions: SliderPositions,
    modifier: Modifier = Modifier,
    colors: SliderColors = SliderDefaults.colors(),
    enabled: Boolean = true,
) {

    val inactiveTickColor = Color.White
    val activeTickColor = Color.White
    val trackBrushStart = Color(0xff000000)
    val trackBrushEnd = Color(0xffffffff)
    val trackBrush = Brush.horizontalGradient(
        listOf(trackBrushStart, trackBrushEnd)
    )
    Canvas(
        modifier
            .fillMaxWidth()
            .height(4.dp)
    ) {
        val isRtl = layoutDirection == LayoutDirection.Rtl
        val sliderLeft = Offset(0f, center.y)
        val sliderRight = Offset(size.width, center.y)
        val sliderStart = if (isRtl) sliderRight else sliderLeft
        val sliderEnd = if (isRtl) sliderLeft else sliderRight
        val tickSize = 2.dp.toPx()
        val trackStrokeWidth = 4.dp.toPx()
        drawLine(
            brush = trackBrush,
            sliderStart,
            sliderEnd,
            trackStrokeWidth,
            StrokeCap.Round
        )
        val sliderValueEnd = Offset(
            sliderStart.x +
                    (sliderEnd.x - sliderStart.x) * sliderPositions.positionFraction,
            center.y
        )

        val sliderValueStart = Offset(
            sliderStart.x +
                    (sliderEnd.x - sliderStart.x) * 0f,
            center.y
        )

        drawLine(
            brush = trackBrush,
            sliderValueStart,
            sliderValueEnd,
            trackStrokeWidth,
            StrokeCap.Round
        )
        sliderPositions.tickFractions.groupBy {
            it > sliderPositions.positionFraction ||
                    it < 0f
        }.forEach { (outsideFraction, list) ->
            drawPoints(
                list.map {
                    Offset(lerp(sliderStart, sliderEnd, it).x, center.y)
                },
                PointMode.Points,
                (if (outsideFraction) inactiveTickColor else activeTickColor),
                tickSize,
                StrokeCap.Round
            )
        }
    }
}
