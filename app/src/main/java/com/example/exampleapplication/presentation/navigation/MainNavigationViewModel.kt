package com.example.exampleapplication.presentation.navigation

import android.text.TextUtils
import androidx.lifecycle.ViewModel
import com.example.common.ui.topappbar.ScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainNavigationViewModel : ViewModel() {

    private val _screen: MutableStateFlow<ScreenModel?> = MutableStateFlow(null)
    val screen: StateFlow<ScreenModel?> = _screen.asStateFlow()

    fun updateTopBarWithScreen(screen: ScreenModel) {
        _screen.value = screen
        TextUtils.isEmpty("")
    }

}
