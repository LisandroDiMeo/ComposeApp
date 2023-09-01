package com.example.exampleapplication.presentation.fragments.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.exampleapplication.R

@OptIn(ExperimentalTextApi::class)
@Preview(device = Devices.NEXUS_5X, showSystemUi = true, showBackground = true)
@Composable
fun CanvasSample() {

    var blendMode by remember { mutableStateOf(0) }
    var currentOffset by remember { mutableStateOf(Offset(0f,0f)) }
    val textMeasure = rememberTextMeasurer()

    Box(modifier = Modifier.fillMaxSize()) {
        Text("Current blend mode is ${provideBlendMode(blendMode)}")
        Image(
            painter = painterResource(id = R.drawable.brc),
            contentDescription = null,
            modifier = Modifier
                .size(256.dp)
                .align(Alignment.Center)
        )
        Canvas(modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    currentOffset = change.position
                }
            }
            .pointerInput(Unit) {
                detectTapGestures {
                    blendMode = if (blendMode == 28) 0 else (blendMode + 1)
                }
            }
        ) {

            drawWithLayer {
                drawText(
                    textMeasurer = textMeasure,
                    text = "Hello",
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 24.sp
                    ),
                    topLeft = currentOffset ,
                )
                drawCircle(
                    color = Color.Transparent,
                    radius = 50f,
                    center,
                    blendMode = BlendMode.SrcOut
                )
            }

        }
    }

}

private fun provideBlendMode(value: Int): BlendMode {
    val constructor = BlendMode::class.java.declaredConstructors[0]
    constructor.isAccessible = true
    return constructor.newInstance(value) as BlendMode
}

fun DrawScope.drawWithLayer(block: DrawScope.() -> Unit) {
    with(drawContext.canvas.nativeCanvas) {
        val checkPoint = saveLayer(null, null)
        block()
        restoreToCount(checkPoint)
    }
}
