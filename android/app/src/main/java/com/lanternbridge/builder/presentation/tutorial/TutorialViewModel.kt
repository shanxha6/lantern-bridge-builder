package com.lanternbridge.builder.presentation.tutorial

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class TutorialStep { TAP_POSTS, LIMITED_PLANKS, LONG_SPANS, HEAVY_CARTS }

class TutorialViewModel : ViewModel() {

    private val _steps = MutableStateFlow(TutorialStep.entries.toList())
    val steps: StateFlow<List<TutorialStep>> = _steps.asStateFlow()
}
