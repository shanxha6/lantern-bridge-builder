package com.lanternbridge.builder.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lanternbridge.builder.presentation.app.AppViewModel
import com.lanternbridge.builder.presentation.gallery.GalleryViewModel
import com.lanternbridge.builder.presentation.game.GameViewModel
import com.lanternbridge.builder.presentation.gameover.GameOverViewModel
import com.lanternbridge.builder.presentation.menu.MenuViewModel
import com.lanternbridge.builder.presentation.settings.SettingsViewModel
import com.lanternbridge.builder.presentation.splash.SplashViewModel
import com.lanternbridge.builder.presentation.tutorial.TutorialViewModel

object AppViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val created: ViewModel = when {
            modelClass.isAssignableFrom(AppViewModel::class.java) -> AppViewModel()
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> SplashViewModel()
            modelClass.isAssignableFrom(MenuViewModel::class.java) ->
                MenuViewModel(ServiceLocator.progress())
            modelClass.isAssignableFrom(GameViewModel::class.java) ->
                GameViewModel(
                    ServiceLocator.findRoute,
                    ServiceLocator.placePlank,
                    ServiceLocator.simulateStep,
                    ServiceLocator.scoreCrossing,
                    ServiceLocator.settings()
                )
            modelClass.isAssignableFrom(GameOverViewModel::class.java) ->
                GameOverViewModel(ServiceLocator.progress())
            modelClass.isAssignableFrom(GalleryViewModel::class.java) ->
                GalleryViewModel(ServiceLocator.progress())
            modelClass.isAssignableFrom(TutorialViewModel::class.java) -> TutorialViewModel()
            modelClass.isAssignableFrom(SettingsViewModel::class.java) ->
                SettingsViewModel(ServiceLocator.settings(), ServiceLocator.progress())
            else -> throw IllegalArgumentException(modelClass.name)
        }
        return created as T
    }
}
