package com.lanternbridge.builder.domain.repository

interface SettingsRepository {
    fun soundEnabled(): Boolean
    fun setSoundEnabled(value: Boolean)
    fun vibrationEnabled(): Boolean
    fun setVibrationEnabled(value: Boolean)
    fun gentleMode(): Boolean
    fun setGentleMode(value: Boolean)
}
