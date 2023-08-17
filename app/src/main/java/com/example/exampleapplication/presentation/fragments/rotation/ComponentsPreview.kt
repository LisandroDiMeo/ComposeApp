package com.example.exampleapplication.presentation.fragments.rotation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.exampleapplication.presentation.fragments.brightness.BrightnessSelector
import kotlin.math.roundToInt

@Preview(device = Devices.NEXUS_5X)
@Composable
fun ComponentsPreview() {
    var angle by remember { mutableStateOf("") }
    var rotationAngle by remember { mutableStateOf(90f) }
    var isVisible by remember { mutableStateOf(true) }
    var brightness by remember { mutableStateOf(.5f) }
    var brightnessSelectorVisibility by remember { mutableStateOf(0f) }
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .height(screenHeight - screenHeight / 3)
                .border(8.dp, Color.Red)
                .pointerInput(true) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            brightnessSelectorVisibility = 0f
                        }
                    ) { change, _ ->
                        brightnessSelectorVisibility = 1f
                        brightness = change.position.x / screenWidth.toPx()
                    }
                }
        ) {
            Image(
                Icons.Filled.Star,
                contentDescription = null,
                modifier = Modifier
                    .rotate(rotationAngle)
                    .alpha(brightness)
                    .align(Alignment.CenterHorizontally)
                    .size(128.dp),
                colorFilter = ColorFilter.tint(Color.Red)
            )
        }
        BrightnessSelector(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 64.dp)
                .offset(y = 64.dp)
                .alpha(brightnessSelectorVisibility)
                .align(Alignment.Center),
            brightness = brightness,
            onBrightnessChanged = {
                brightness = it
            }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .offset(y = 170.dp)
        ) {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = angle,
                color = Color.Cyan,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
            ImageRotation(
                isVisible,
                canvasHeight = 412.dp,
                offsetY = 0.dp,
                onCrossPressed = { isVisible = false }
            ) {
                val absoluteAngle = (90 - it.roundToInt()) % 360
                val finalAngle =
                    if (absoluteAngle < 0) (absoluteAngle + 360) % 360 else absoluteAngle
                rotationAngle = finalAngle.toFloat()
                angle = finalAngle.toString() + "º (${it.roundToInt()})"
            }
        }
        Column(
            modifier = Modifier.align(Alignment.BottomCenter),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
        ) {
            AnglePreview(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .alpha(
                        if (isVisible) 0f else 1f
                    ),
                angle = angle.split("(").first()
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Blue)
                    .clickable { /*Little hack to avoid drag events on this row.*/ },
                horizontalArrangement = Arrangement.spacedBy(
                    32.dp,
                    Alignment.CenterHorizontally
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        Icons.Filled.Settings,
                        contentDescription = "Settings",
                        tint = Color.White
                    )
                }
                IconButton(onClick = { isVisible = true }) {
                    Icon(Icons.Filled.Star, contentDescription = "Settings", tint = Color.White)
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Filled.Menu, contentDescription = "Settings", tint = Color.White)
                }
            }
        }

    }
//    Column(
//        Modifier
//            .fillMaxSize()
//            .background(Color.Black)
//            .pointerInput(true) {
//                detectHorizontalDragGestures { change, _ ->
//                    brightness = change.position.x / screenWidth.toPx()
//                }
//            },
//        verticalArrangement = Arrangement.Center
//    ) {
//        Image(
//            painter = painterResource(id = R.drawable.ic_launcher_foreground),
//            contentDescription = null,
//            modifier = Modifier
//                .rotate(rotationAngle)
//                .align(Alignment.CenterHorizontally)
//                .size(128.dp),
//            colorFilter = ColorFilter.tint(Color.Red)
//        )
//        BrightnessSelector(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            brightness = brightness,
//            onBrightnessChanged = {
//                brightness = it
//            }
//        )
//        Column(
//            modifier = Modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.Bottom
//        ) {
//            Box(contentAlignment = Alignment.Center) {
//                Text(
//                    angle,
//                    textAlign = TextAlign.Center,
//                    modifier = Modifier.fillMaxWidth()
//                )
//                ImageRotation(
//                    isVisible,
//                    canvasHeight = 412.dp,
//                    onCrossPressed = { isVisible = false }
//                ) {
//                    val absoluteAngle = (90 - it.roundToInt()) % 360
//                    val finalAngle =
//                        if (absoluteAngle < 0) (absoluteAngle + 360) % 360 else absoluteAngle
//                    rotationAngle = finalAngle.toFloat()
//                    angle = finalAngle.toString() + "º (${it.roundToInt()})"
//                }
//            }
//
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(Color.Blue)
//                    .clickable { /*Little hack to avoid drag events on this row.*/ },
//                horizontalArrangement = Arrangement.spacedBy(
//                    32.dp,
//                    Alignment.CenterHorizontally
//                ),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                IconButton(onClick = { /*TODO*/ }) {
//                    Icon(
//                        Icons.Filled.Settings,
//                        contentDescription = "Settings",
//                        tint = Color.White
//                    )
//                }
//                IconButton(onClick = { isVisible = true }) {
//                    Icon(Icons.Filled.Star, contentDescription = "Settings", tint = Color.White)
//                }
//                IconButton(onClick = { /*TODO*/ }) {
//                    Icon(Icons.Filled.Menu, contentDescription = "Settings", tint = Color.White)
//                }
//            }
//        }
//
//
//    }
}
