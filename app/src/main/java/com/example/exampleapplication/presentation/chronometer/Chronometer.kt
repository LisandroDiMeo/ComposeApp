package com.example.exampleapplication.presentation.chronometer

import java.util.Timer
import java.util.TimerTask

class Chronometer(
    private val onReached: () -> Unit,
    private val timeMs: Long
) {
    private var timer: Timer = Timer()

    private fun provideTask(): TimerTask {
        return object : TimerTask() {
            override fun run() {
                onReached()
            }
        }
    }

    fun start() {
        timer.schedule(provideTask(), timeMs)
    }

    fun restart() {
        timer.cancel()
        timer = Timer()
        timer.schedule(provideTask(), timeMs)
    }

    fun cancel() {
        timer.cancel()
    }
}
