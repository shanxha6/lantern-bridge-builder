package com.lanternbridge.builder.presentation.app

import androidx.lifecycle.ViewModel
import com.lanternbridge.builder.core.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel : ViewModel() {

    private val backStack = ArrayList<Screen>()
    private var roundCounter = 0

    private val _screen = MutableStateFlow<Screen>(Screen.Splash)
    val screen: StateFlow<Screen> = _screen.asStateFlow()

    private val _canGoBack = MutableStateFlow(false)
    val canGoBack: StateFlow<Boolean> = _canGoBack.asStateFlow()

    fun navigate(target: Screen) {
        val current = _screen.value
        if (current != Screen.Splash) backStack.add(current)
        show(target)
    }

    fun replace(target: Screen) {
        show(target)
    }

    fun openRound(districtId: Int, levelIndex: Int) {
        roundCounter += 1
        navigate(Screen.Game(districtId, levelIndex, roundCounter))
    }

    fun replayRound(districtId: Int, levelIndex: Int) {
        roundCounter += 1
        replace(Screen.Game(districtId, levelIndex, roundCounter))
    }

    fun back(): Boolean {
        if (backStack.isEmpty()) return false
        val previous = backStack.removeAt(backStack.size - 1)
        _screen.value = previous
        _canGoBack.value = backStack.isNotEmpty()
        return true
    }

    fun backToMenu() {
        backStack.clear()
        show(Screen.Menu)
    }

    private fun show(target: Screen) {
        _screen.value = target
        _canGoBack.value = backStack.isNotEmpty()
    }
}
