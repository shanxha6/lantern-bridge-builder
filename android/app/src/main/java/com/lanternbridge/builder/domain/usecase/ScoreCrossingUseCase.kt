package com.lanternbridge.builder.domain.usecase

import com.lanternbridge.builder.core.config.GameConfig

class ScoreCrossingUseCase {

    operator fun invoke(planksLeft: Int, plankBudget: Int): Int {
        if (plankBudget <= 0) return 1
        val ratio = planksLeft.toFloat() / plankBudget.toFloat()
        return when {
            ratio >= GameConfig.STAR3_RATIO -> 3
            ratio >= GameConfig.STAR2_RATIO -> 2
            else -> 1
        }
    }
}
