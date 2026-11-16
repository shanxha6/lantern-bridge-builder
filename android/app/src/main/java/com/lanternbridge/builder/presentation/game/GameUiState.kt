package com.lanternbridge.builder.presentation.game

import com.lanternbridge.builder.domain.model.CrossingResult
import com.lanternbridge.builder.domain.model.GamePhase
import com.lanternbridge.builder.domain.model.Plank
import com.lanternbridge.builder.domain.model.Post

data class GameUiState(
    val loaded: Boolean = false,
    val districtId: Int = 1,
    val levelIndex: Int = 0,
    val posts: List<Post> = emptyList(),
    val finishIndex: Int = 0,
    val planks: List<Plank> = emptyList(),
    val plankBudget: Int = 0,
    val weight: Float = 1f,
    val selectedPost: Int = -1,
    val reachable: Set<Int> = emptySet(),
    val litPosts: Set<Int> = emptySet(),
    val phase: GamePhase = GamePhase.BUILD,
    val cartX: Float = 0f,
    val cartY: Float = 0f,
    val cartAngle: Float = 0f,
    val activePlank: Int = -1,
    val activeSag: Float = 0f,
    val snappedPlank: Int = -1,
    val fallProgress: Float = 0f,
    val crossed: Boolean = false,
    val routeComplete: Boolean = false,
    val gentleMode: Boolean = false
) {
    val planksUsed: Int get() = planks.size
    val planksLeft: Int get() = (plankBudget - planks.size).coerceAtLeast(0)
}

sealed interface GameEvent {
    data class RoundEnded(val result: CrossingResult) : GameEvent
}
