package com.example.exampleapplication.presentation.chronometer

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.MutableStateFlow

class SessionExpirationObserver(
    private val timeToExpireMs: Long = 20000L,
) : DefaultLifecycleObserver {

    val state: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private var hasStarted = false
    private val chronometer = Chronometer(
        onReached = {
            Log.i(EXPIRED_MSG_TAG, EXPIRED_MSG)
            state.value = true
        },
        timeToExpireMs
    )

    fun interactionReceived() {
        if (!state.value)
            chronometer.restart()
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        if (!state.value) {
            if (hasStarted) chronometer.restart()
            else {
                hasStarted = true
                chronometer.start()
            }
        }
    }

    companion object {
        private const val EXPIRED_MSG_TAG = "EXPIRED_MSG"
        private const val EXPIRED_MSG = "SESSION EXPIRED"
    }

}

interface SessionState {
    fun actionForState()
}

class ActiveSession: SessionState {
    override fun actionForState() {
        TODO("Not yet implemented")
    }
}

class ExpiredSession(private val navHostController: NavHostController): SessionState {
    override fun actionForState() {
        navHostController.navigate("OTHER_ROUTE")
    }
}
