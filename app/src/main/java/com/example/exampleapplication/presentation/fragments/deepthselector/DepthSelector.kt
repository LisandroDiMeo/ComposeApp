package com.example.exampleapplication.presentation.fragments.deepthselector

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalTextApi::class)
@Preview
@Composable
fun DepthSelector(
    modifier: Modifier = Modifier,
) {
    var indicators by remember { mutableStateOf(15) }
    var dragContentAmount by remember { mutableStateOf(0) }
    val textMeasurer = rememberTextMeasurer()
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(true) {
                detectVerticalDragGestures { change, dragAmount ->
                    Log.i("DRAG", dragContentAmount.toString())
                    dragContentAmount +=
                        if (dragAmount < 0) 1
                        else if(dragContentAmount == 0) 0
                        else -1
                    indicators = 10 + (dragContentAmount / 3)
                }
            }
    ) {
        val height = size.height
        val spacingBetweenIndicators = (height / indicators) * .90f
        for(indicator in 0 .. indicators) {
            if ( indicator % 5 == 0 ) {
                val text = if (indicator < 10) "0$indicator" else indicator.toString()
                drawText(
                    textMeasurer = textMeasurer,
                    text = text,
                    topLeft = Offset(x = center.x*.95f , y = (spacingBetweenIndicators * (indicator))),
                    style = TextStyle(color = Color.Black)
                )
            } else {
                drawCircle(
                    color = Color.Black,
                    radius = 4f,
                    center = Offset(x = center.x , y = (spacingBetweenIndicators * (indicator)))
                )
            }

        }
    }
}
