package com.lanternbridge.builder.presentation.settings

import androidx.lifecycle.ViewModel
import com.lanternbridge.builder.domain.repository.ProgressRepository
import com.lanternbridge.builder.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingsViewModel(
    private val settings: SettingsRepository,
    private val progress: ProgressRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState())
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        _state.update {
            it.copy(
                sound = settings.soundEnabled(),
                vibration = settings.vibrationEnabled(),
                gentleMode = settings.gentleMode(),
                lanterns = progress.lanternsLit(),
                stars = progress.totalStars()
            )
        }
    }

    fun setSound(value: Boolean) {
        settings.setSoundEnabled(value)
        _state.update { it.copy(sound = value) }
    }

    fun setVibration(value: Boolean) {
        settings.setVibrationEnabled(value)
        _state.update { it.copy(vibration = value) }
    }

    fun setGentleMode(value: Boolean) {
        settings.setGentleMode(value)
        _state.update { it.copy(gentleMode = value) }
    }

    fun askReset() {
        _state.update { it.copy(showResetDialog = true) }
    }

    fun dismissReset() {
        _state.update { it.copy(showResetDialog = false) }
    }

    fun confirmReset() {
        progress.reset()
        _state.update { it.copy(showResetDialog = false) }
        refresh()
    }
}
