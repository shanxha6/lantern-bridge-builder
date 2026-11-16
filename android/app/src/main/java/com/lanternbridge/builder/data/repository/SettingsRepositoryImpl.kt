package com.lanternbridge.builder.data.repository

import com.lanternbridge.builder.data.local.LanternPrefs
import com.lanternbridge.builder.domain.repository.SettingsRepository

class SettingsRepositoryImpl(private val prefs: LanternPrefs) : SettingsRepository {

    override fun soundEnabled(): Boolean = prefs.readBoolean(LanternPrefs.KEY_SOUND, true)

    override fun setSoundEnabled(value: Boolean) = prefs.writeBoolean(LanternPrefs.KEY_SOUND, value)

    override fun vibrationEnabled(): Boolean = prefs.readBoolean(LanternPrefs.KEY_VIBRATION, true)

    override fun setVibrationEnabled(value: Boolean) =
        prefs.writeBoolean(LanternPrefs.KEY_VIBRATION, value)

    override fun gentleMode(): Boolean = prefs.readBoolean(LanternPrefs.KEY_GENTLE, false)

    override fun setGentleMode(value: Boolean) = prefs.writeBoolean(LanternPrefs.KEY_GENTLE, value)
}
