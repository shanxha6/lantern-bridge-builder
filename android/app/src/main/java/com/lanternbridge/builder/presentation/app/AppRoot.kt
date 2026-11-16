package com.lanternbridge.builder.presentation.app

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lanternbridge.builder.core.di.AppViewModelFactory
import com.lanternbridge.builder.core.navigation.Screen
import com.lanternbridge.builder.presentation.gallery.GalleryScreen
import com.lanternbridge.builder.presentation.game.GameScreen
import com.lanternbridge.builder.presentation.gameover.GameOverScreen
import com.lanternbridge.builder.presentation.menu.MenuScreen
import com.lanternbridge.builder.presentation.settings.SettingsScreen
import com.lanternbridge.builder.presentation.splash.SplashScreen
import com.lanternbridge.builder.presentation.tutorial.TutorialScreen

@Composable
fun AppRoot() {
    val appViewModel: AppViewModel = viewModel(factory = AppViewModelFactory)
    val screen by appViewModel.screen.collectAsStateWithLifecycle()
    val canGoBack by appViewModel.canGoBack.collectAsStateWithLifecycle()

    BackHandler(enabled = canGoBack) {
        appViewModel.back()
    }

    AnimatedContent(
        targetState = screen,
        modifier = Modifier.fillMaxSize(),
        transitionSpec = {
            if (targetState is Screen.Game) {
                (slideInHorizontally(animationSpec = tween(durationMillis = 220)) { full ->
                    full / 3
                } + fadeIn(animationSpec = tween(durationMillis = 220))) togetherWith
                    (slideOutHorizontally(animationSpec = tween(durationMillis = 180)) { full ->
                        -full / 6
                    } + fadeOut(animationSpec = tween(durationMillis = 180)))
            } else {
                fadeIn(animationSpec = tween(durationMillis = 240)) togetherWith
                    fadeOut(animationSpec = tween(durationMillis = 200))
            }
        },
        label = "appScreen"
    ) { current ->
        when (current) {
            is Screen.Splash -> SplashScreen(
                onFinished = { appViewModel.replace(Screen.Menu) }
            )
            is Screen.Menu -> MenuScreen(
                onPlay = { districtId, levelIndex ->
                    appViewModel.openRound(districtId, levelIndex)
                },
                onGallery = { appViewModel.navigate(Screen.Gallery) },
                onTutorial = { appViewModel.navigate(Screen.Tutorial) },
                onSettings = { appViewModel.navigate(Screen.Settings) }
            )
            is Screen.Game -> GameScreen(
                districtId = current.districtId,
                levelIndex = current.levelIndex,
                round = current.round,
                onRoundEnd = { result -> appViewModel.replace(result) },
                onMenu = { appViewModel.backToMenu() }
            )
            is Screen.GameOver -> GameOverScreen(
                result = current,
                onReplay = { districtId, levelIndex ->
                    appViewModel.replayRound(districtId, levelIndex)
                },
                onNext = { districtId, levelIndex ->
                    appViewModel.replayRound(districtId, levelIndex)
                },
                onMenu = { appViewModel.backToMenu() }
            )
            is Screen.Gallery -> GalleryScreen(
                onBack = { appViewModel.back() }
            )
            is Screen.Tutorial -> TutorialScreen(
                onBack = { appViewModel.back() },
                onStart = { appViewModel.backToMenu() }
            )
            is Screen.Settings -> SettingsScreen(
                onBack = { appViewModel.back() }
            )
        }
    }
}
