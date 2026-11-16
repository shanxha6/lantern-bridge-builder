package com.lanternbridge.builder.core.navigation

sealed interface Screen {
    data object Splash : Screen
    data object Menu : Screen
    data class Game(val districtId: Int, val levelIndex: Int, val round: Int) : Screen
    data class GameOver(
        val districtId: Int,
        val levelIndex: Int,
        val crossed: Boolean,
        val stars: Int,
        val planksLeft: Int,
        val planksUsed: Int
    ) : Screen
    data object Gallery : Screen
    data object Tutorial : Screen
    data object Settings : Screen
}
