package com.lanternbridge.builder.presentation.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lanternbridge.builder.R
import com.lanternbridge.builder.core.di.AppViewModelFactory
import com.lanternbridge.builder.core.navigation.Screen
import com.lanternbridge.builder.core.theme.InkText
import com.lanternbridge.builder.core.theme.MintAccent
import com.lanternbridge.builder.core.theme.NightDeep
import com.lanternbridge.builder.core.theme.NightRiver
import com.lanternbridge.builder.core.theme.PaperBg
import com.lanternbridge.builder.core.theme.PaperLight
import com.lanternbridge.builder.core.ui.Labels
import com.lanternbridge.builder.core.ui.PlankPips
import com.lanternbridge.builder.core.ui.PrimaryButton
import com.lanternbridge.builder.core.ui.ScreenBackground
import com.lanternbridge.builder.core.ui.ScreenHeader
import com.lanternbridge.builder.domain.model.GamePhase
import com.lanternbridge.builder.presentation.dialog.PauseDialog
import com.lanternbridge.builder.presentation.dialog.QuitRoundDialog

@Composable
fun GameScreen(
    districtId: Int,
    levelIndex: Int,
    round: Int,
    onRoundEnd: (Screen.GameOver) -> Unit,
    onMenu: () -> Unit
) {
    val gameViewModel: GameViewModel = viewModel(factory = AppViewModelFactory)
    val state by gameViewModel.state.collectAsStateWithLifecycle()
    var showPause by rememberSaveable { mutableStateOf(false) }
    var showQuit by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(round, districtId, levelIndex) {
        gameViewModel.open(districtId, levelIndex)
    }

    LaunchedEffect(Unit) {
        gameViewModel.events.collect { event ->
            when (event) {
                is GameEvent.RoundEnded -> onRoundEnd(
                    Screen.GameOver(
                        districtId = event.result.districtId,
                        levelIndex = event.result.levelIndex,
                        crossed = event.result.crossed,
                        stars = event.result.stars,
                        planksLeft = event.result.planksLeft,
                        planksUsed = event.result.planksUsed
                    )
                )
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose { gameViewModel.pause() }
    }

    ScreenBackground(
        overlay = Brush.verticalGradient(
            listOf(
                NightDeep.copy(alpha = 0.72f),
                NightRiver.copy(alpha = 0.78f),
                NightDeep.copy(alpha = 0.90f)
            )
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 14.dp)
        ) {
            ScreenHeader(
                title = stringResource(Labels.districtName(state.districtId)),
                subtitle = stringResource(
                    R.string.crossing_label,
                    state.districtId,
                    state.levelIndex + 1
                ),
                titleColor = PaperLight,
                onBack = { showQuit = true },
                backLabel = stringResource(R.string.cd_menu),
                trailing = {
                    IconButton(
                        onClick = { showPause = true },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.icon_pause),
                            contentDescription = stringResource(R.string.cd_pause),
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            )

            Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
                BridgeBoard(
                    state = state,
                    onPostTap = { index -> gameViewModel.onPostTap(index) },
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 6.dp,
                        shape = RoundedCornerShape(22.dp),
                        clip = false
                    )
                    .background(
                        color = PaperBg.copy(alpha = 0.95f),
                        shape = RoundedCornerShape(22.dp)
                    )
                    .border(
                        width = 1.5.dp,
                        color = InkText.copy(alpha = 0.20f),
                        shape = RoundedCornerShape(22.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(
                            R.string.planks_counter,
                            state.planksLeft,
                            state.plankBudget
                        ),
                        style = MaterialTheme.typography.titleMedium,
                        color = InkText,
                        modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    PlankPips(
                        used = state.planksUsed,
                        total = state.plankBudget,
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(
                        onClick = { gameViewModel.onUndo() },
                        enabled = state.phase == GamePhase.BUILD && state.planks.isNotEmpty()
                    ) {
                        Text(
                            text = stringResource(R.string.undo),
                            style = MaterialTheme.typography.labelLarge,
                            color = if (state.planks.isEmpty()) {
                                InkText.copy(alpha = 0.40f)
                            } else {
                                InkText
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                if (state.phase == GamePhase.BUILD) {
                    PrimaryButton(
                        text = stringResource(R.string.roll),
                        onClick = { gameViewModel.onRoll() },
                        leadingIcon = Icons.Default.PlayArrow,
                        stateHint = if (state.routeComplete) {
                            stringResource(R.string.bridge_state_ready)
                        } else {
                            stringResource(R.string.bridge_state_open)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    Column(
                        modifier = Modifier.fillMaxWidth().height(60.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.rolling),
                            style = MaterialTheme.typography.labelLarge,
                            color = InkText
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            color = MintAccent,
                            trackColor = Color.Transparent,
                            modifier = Modifier.fillMaxWidth().height(4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    if (showPause) {
        PauseDialog(
            onResume = { showPause = false },
            onRestart = {
                showPause = false
                gameViewModel.restart()
            },
            onMenu = {
                showPause = false
                onMenu()
            }
        )
    }

    if (showQuit) {
        QuitRoundDialog(
            onCancel = { showQuit = false },
            onConfirm = {
                showQuit = false
                onMenu()
            }
        )
    }
}
