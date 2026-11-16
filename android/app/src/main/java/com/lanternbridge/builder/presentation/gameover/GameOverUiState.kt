package com.lanternbridge.builder.presentation.gameover

data class GameOverUiState(
    val ready: Boolean = false,
    val districtId: Int = 1,
    val levelIndex: Int = 0,
    val crossed: Boolean = false,
    val stars: Int = 0,
    val planksLeft: Int = 0,
    val planksUsed: Int = 0,
    val lanternsLit: Int = 0,
    val hasNext: Boolean = false,
    val nextDistrict: Int = 1,
    val nextLevel: Int = 0,
    val designUnlocked: Boolean = false
)
