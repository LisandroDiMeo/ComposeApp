package com.example.exampleapplication.presentation.navigation

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking

@Preview
@Composable
fun NavHost() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        val navigationViewModel = viewModel(
            modelClass = NavigationViewModel::class.java
        )
        LaunchedEffect(Unit) {
            navigationViewModel.gatherTitles()
        }
        Text(text = "Hello")
    }
}

class NavigationViewModel : ViewModel() {
    private val _title: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    val title: StateFlow<List<String>> = _title.asStateFlow()

    fun gatherTitles() {
        viewModelScope.launch(Dispatchers.IO) {
            val request = launch {
                // it spawns two other jobs
                launch(Job()) {
                    println("job1: I run in my own Job and execute independently!")
                    delay(1000)
                    println("job1: I am not affected by cancellation of the request")
                }
                // and the other inherits the parent context
                launch {
                    delay(100)
                    println("job2: I am a child of the request coroutine")
                    delay(1000)
                    println("job2: I will not execute this line if my parent request is cancelled")
                }
            }
            delay(500)
            request.cancel() // cancel processing of the request
            println("main: Who has survived request cancellation?")
            delay(1000)

        }
    }
}
