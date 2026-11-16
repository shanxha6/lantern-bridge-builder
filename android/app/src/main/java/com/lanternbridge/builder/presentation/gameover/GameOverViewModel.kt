package com.lanternbridge.builder.presentation.gameover

import androidx.lifecycle.ViewModel
import com.lanternbridge.builder.core.config.GameConfig
import com.lanternbridge.builder.domain.model.CrossingResult
import com.lanternbridge.builder.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameOverViewModel(private val progress: ProgressRepository) : ViewModel() {

    private val _state = MutableStateFlow(GameOverUiState())
    val state: StateFlow<GameOverUiState> = _state.asStateFlow()

    private var appliedKey: String = ""

    fun open(result: CrossingResult) {
        val key = result.districtId.toString() + ":" + result.levelIndex + ":" +
            result.crossed + ":" + result.stars + ":" + result.planksLeft
        if (appliedKey == key) return
        appliedKey = key
        val completedBefore = progress.completedInDistrict(result.districtId)
        if (result.crossed && result.stars > 0) {
            progress.saveStars(result.districtId, result.levelIndex, result.stars)
        }
        val completedAfter = progress.completedInDistrict(result.districtId)
        val unlockedDesign = completedBefore < 8 && completedAfter >= 8 ||
            completedBefore < GameConfig.LEVELS_PER_DISTRICT &&
            completedAfter >= GameConfig.LEVELS_PER_DISTRICT
        val nextLevelInDistrict = result.levelIndex + 1
        val hasNextInDistrict = nextLevelInDistrict < GameConfig.LEVELS_PER_DISTRICT
        val nextDistrict = if (hasNextInDistrict) {
            result.districtId
        } else {
            (result.districtId + 1).coerceAtMost(GameConfig.DISTRICT_COUNT)
        }
        val nextLevel = if (hasNextInDistrict) nextLevelInDistrict else 0
        _state.value = GameOverUiState(
            ready = true,
            districtId = result.districtId,
            levelIndex = result.levelIndex,
            crossed = result.crossed,
            stars = result.stars,
            planksLeft = result.planksLeft,
            planksUsed = result.planksUsed,
            lanternsLit = progress.lanternsLit(),
            hasNext = result.crossed && progress.isUnlocked(nextDistrict),
            nextDistrict = nextDistrict,
            nextLevel = nextLevel,
            designUnlocked = result.crossed && unlockedDesign
        )
    }
}
