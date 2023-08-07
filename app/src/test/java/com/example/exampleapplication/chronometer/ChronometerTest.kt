package com.example.exampleapplication.chronometer

import com.example.exampleapplication.presentation.chronometer.Chronometer
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.lang.IllegalStateException

@RunWith(JUnit4::class)
class ChronometerTest {

    @Test
    fun `chronometer does not execute task before timer ends`(){
        var executed = false
        val chronometer = Chronometer(
            onReached = { executed = true },
            timeMs = 500L
        )
        chronometer.start()
        runBlocking {
            delay(350L)
        }
        assertEquals(false, executed)
    }

    @Test
    fun `chronometer execute task after timer ends`(){
        var executed = false
        val chronometer = Chronometer(
            onReached = { executed = true },
            timeMs = 500L
        )
        chronometer.start()
        runBlocking {
            delay(510L)
        }
        assertEquals(true, executed)
    }

    @Test
    fun `chronometer does not execute task if restart occur`(){
        var executed = false
        val chronometer = Chronometer(
            onReached = { executed = true },
            timeMs = 500L
        )
        chronometer.start()
        runBlocking {
            delay(200L)
            chronometer.restart()
            delay(400L)
        }
        assertEquals(false, executed)
    }

    @Test
    fun `after chronometer cancelled cannot be started`() {
        val chronometer = Chronometer(
            onReached = { assertTrue(false) /* If reached, test fails */ },
            timeMs = 500L
        )
        chronometer.start()
        runBlocking {
            delay(200L)
            chronometer.cancel()
        }
        try {
            chronometer.start()
        } catch (e: IllegalStateException) {
            assertEquals(e.message, "Timer already cancelled.")
        }
    }
}
