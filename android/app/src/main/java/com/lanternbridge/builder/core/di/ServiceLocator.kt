package com.lanternbridge.builder.core.di

import android.content.Context
import com.lanternbridge.builder.data.local.LanternPrefs
import com.lanternbridge.builder.data.repository.ProgressRepositoryImpl
import com.lanternbridge.builder.data.repository.SettingsRepositoryImpl
import com.lanternbridge.builder.domain.repository.ProgressRepository
import com.lanternbridge.builder.domain.repository.SettingsRepository
import com.lanternbridge.builder.domain.usecase.FindRouteUseCase
import com.lanternbridge.builder.domain.usecase.PlacePlankUseCase
import com.lanternbridge.builder.domain.usecase.ScoreCrossingUseCase
import com.lanternbridge.builder.domain.usecase.SimulateStepUseCase

object ServiceLocator {

    private var progressRepository: ProgressRepository? = null
    private var settingsRepository: SettingsRepository? = null

    val findRoute = FindRouteUseCase()
    val placePlank = PlacePlankUseCase()
    val simulateStep = SimulateStepUseCase()
    val scoreCrossing = ScoreCrossingUseCase()

    fun init(context: Context) {
        if (progressRepository == null) {
            val prefs = LanternPrefs(context)
            progressRepository = ProgressRepositoryImpl(prefs)
            settingsRepository = SettingsRepositoryImpl(prefs)
        }
    }

    fun progress(): ProgressRepository = requireNotNull(progressRepository)

    fun settings(): SettingsRepository = requireNotNull(settingsRepository)
}
