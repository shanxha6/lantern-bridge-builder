package com.lanternbridge.builder.presentation.settings

data class SettingsUiState(
    val sound: Boolean = true,
    val vibration: Boolean = true,
    val gentleMode: Boolean = false,
    val showResetDialog: Boolean = false,
    val lanterns: Int = 0,
    val stars: Int = 0
)
