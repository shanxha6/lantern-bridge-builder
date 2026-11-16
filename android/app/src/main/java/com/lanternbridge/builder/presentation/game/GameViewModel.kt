package com.lanternbridge.builder.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lanternbridge.builder.core.config.GameConfig
import com.lanternbridge.builder.data.sample.SampleData
import com.lanternbridge.builder.domain.model.CrossingResult
import com.lanternbridge.builder.domain.model.GamePhase
import com.lanternbridge.builder.domain.model.Plank
import com.lanternbridge.builder.domain.repository.SettingsRepository
import com.lanternbridge.builder.domain.usecase.FindRouteUseCase
import com.lanternbridge.builder.domain.usecase.PlacePlankUseCase
import com.lanternbridge.builder.domain.usecase.ScoreCrossingUseCase
import com.lanternbridge.builder.domain.usecase.SimulateStepUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.atan2

class GameViewModel(
    private val findRoute: FindRouteUseCase,
    private val placePlank: PlacePlankUseCase,
    private val simulate: SimulateStepUseCase,
    private val score: ScoreCrossingUseCase,
    private val settings: SettingsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(GameUiState())
    val state: StateFlow<GameUiState> = _state.asStateFlow()

    private val eventChannel = Channel<GameEvent>(Channel.BUFFERED)
    val events: Flow<GameEvent> = eventChannel.receiveAsFlow()

    private var loopJob: Job? = null

    fun open(districtId: Int, levelIndex: Int) {
        loopJob?.cancel()
        loopJob = null
        val spec = SampleData.level(districtId, levelIndex)
        val start = spec.posts[0]
        _state.value = GameUiState(
            loaded = true,
            districtId = spec.districtId,
            levelIndex = spec.levelIndex,
            posts = spec.posts,
            finishIndex = spec.finishIndex,
            plankBudget = spec.plankBudget,
            weight = spec.weight,
            cartX = start.x,
            cartY = start.y - GameConfig.CART_LIFT_UNITS,
            litPosts = setOf(0),
            gentleMode = settings.gentleMode()
        )
    }

    fun restart() {
        val current = _state.value
        if (current.loaded) open(current.districtId, current.levelIndex)
    }

    fun onPostTap(index: Int) {
        val current = _state.value
        if (current.phase != GamePhase.BUILD || index !in current.posts.indices) return
        val selected = current.selectedPost
        if (selected == index) {
            _state.update { it.copy(selectedPost = -1, reachable = emptySet()) }
            return
        }
        if (selected == -1) {
            _state.update {
                it.copy(
                    selectedPost = index,
                    reachable = placePlank.reachable(it.posts, index)
                )
            }
            return
        }
        if (placePlank.canPlace(
                current.posts,
                current.planks,
                selected,
                index,
                current.planksLeft
            )
        ) {
            val updated = current.planks + Plank(selected, index)
            _state.update {
                it.copy(
                    planks = updated,
                    selectedPost = -1,
                    reachable = emptySet(),
                    routeComplete = findRoute.route(updated, 0, it.finishIndex) != null
                )
            }
        } else {
            _state.update {
                it.copy(
                    selectedPost = index,
                    reachable = placePlank.reachable(it.posts, index)
                )
            }
        }
    }

    fun onUndo() {
        val current = _state.value
        if (current.phase != GamePhase.BUILD || current.planks.isEmpty()) return
        val updated = current.planks.dropLast(1)
        _state.update {
            it.copy(
                planks = updated,
                selectedPost = -1,
                reachable = emptySet(),
                routeComplete = findRoute.route(updated, 0, it.finishIndex) != null
            )
        }
    }

    fun onRoll() {
        val current = _state.value
        if (!current.loaded || current.phase != GamePhase.BUILD) return
        val route = findRoute.bestEffort(current.posts, current.planks, 0, current.finishIndex)
        _state.update {
            it.copy(
                phase = GamePhase.ROLLING,
                selectedPost = -1,
                reachable = emptySet(),
                fallProgress = 0f,
                snappedPlank = -1
            )
        }
        loopJob?.cancel()
        loopJob = viewModelScope.launch {
            var snapped = false
            var segment = 0
            while (isActive && segment < route.planks.size) {
                val plank = route.planks[segment]
                val index = current.planks.indexOfFirst { candidate ->
                    sameEdge(candidate, plank)
                }
                val from = _state.value.posts[plank.from]
                val to = _state.value.posts[plank.to]
                val span = placePlank.span(from, to)
                val sag = simulate.sag(span, current.weight)
                val breaks = simulate.snaps(span, current.weight)
                val angle = Math.toDegrees(
                    atan2((to.y - from.y).toDouble(), (to.x - from.x).toDouble())
                ).toFloat()
                var travel = 0f
                while (isActive && travel < 1f) {
                    delay(GameConfig.TICK_MS)
                    val previous = travel
                    travel = simulate.advance(travel, span, GameConfig.TICK_MS).coerceAtMost(1f)
                    val bend = sag * 4f * travel * (1f - travel)
                    _state.update {
                        it.copy(
                            cartX = from.x + (to.x - from.x) * travel,
                            cartY = from.y + (to.y - from.y) * travel + bend -
                                GameConfig.CART_LIFT_UNITS,
                            cartAngle = angle,
                            activePlank = index,
                            activeSag = sag * 4f * travel * (1f - travel)
                        )
                    }
                    if (previous < 0.5f && travel >= 0.5f && breaks) {
                        snapped = true
                        _state.update { it.copy(snappedPlank = index) }
                        break
                    }
                }
                if (snapped) break
                val reached = plank.to
                _state.update { it.copy(litPosts = it.litPosts + reached) }
                segment += 1
            }
            settle(crossed = !snapped && route.complete)
        }
    }

    fun pause() {
        loopJob?.cancel()
        loopJob = null
        val current = _state.value
        if (!current.loaded || current.phase == GamePhase.BUILD) return
        val start = current.posts[0]
        _state.update {
            it.copy(
                phase = GamePhase.BUILD,
                litPosts = setOf(0),
                cartX = start.x,
                cartY = start.y - GameConfig.CART_LIFT_UNITS,
                cartAngle = 0f,
                activePlank = -1,
                activeSag = 0f,
                snappedPlank = -1,
                fallProgress = 0f
            )
        }
    }

    private suspend fun settle(crossed: Boolean) {
        _state.update { it.copy(phase = GamePhase.SETTLING, crossed = crossed) }
        val duration = if (crossed) {
            GameConfig.WIN_HANDOFF_MS
        } else if (_state.value.gentleMode) {
            GameConfig.SNAP_FALL_MS + 240L
        } else {
            GameConfig.SNAP_FALL_MS
        }
        val steps = (duration / GameConfig.TICK_MS).toInt().coerceAtLeast(1)
        for (step in 1..steps) {
            delay(GameConfig.TICK_MS)
            val ratio = step.toFloat() / steps.toFloat()
            _state.update { it.copy(fallProgress = ratio) }
        }
        val current = _state.value
        val stars = if (crossed) score(current.planksLeft, current.plankBudget) else 0
        eventChannel.send(
            GameEvent.RoundEnded(
                CrossingResult(
                    districtId = current.districtId,
                    levelIndex = current.levelIndex,
                    crossed = crossed,
                    stars = stars,
                    planksLeft = current.planksLeft,
                    planksUsed = current.planksUsed
                )
            )
        )
    }

    private fun sameEdge(a: Plank, b: Plank): Boolean =
        (a.from == b.from && a.to == b.to) || (a.from == b.to && a.to == b.from)

    override fun onCleared() {
        super.onCleared()
        loopJob?.cancel()
    }
}
