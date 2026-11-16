package com.lanternbridge.builder.presentation.gameover

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lanternbridge.builder.R
import com.lanternbridge.builder.core.di.AppViewModelFactory
import com.lanternbridge.builder.core.navigation.Screen
import com.lanternbridge.builder.core.theme.DustyRose
import com.lanternbridge.builder.core.theme.InkText
import com.lanternbridge.builder.core.theme.LanternAmber
import com.lanternbridge.builder.core.theme.MintAccent
import com.lanternbridge.builder.core.theme.NightDeep
import com.lanternbridge.builder.core.theme.NightRiver
import com.lanternbridge.builder.core.theme.PaperBg
import com.lanternbridge.builder.core.theme.PaperLight
import com.lanternbridge.builder.core.theme.PlumPrimary
import com.lanternbridge.builder.core.ui.GlassCard
import com.lanternbridge.builder.core.ui.PrimaryButton
import com.lanternbridge.builder.core.ui.ScreenBackground
import com.lanternbridge.builder.core.ui.SecondaryButton
import com.lanternbridge.builder.core.ui.StarRow
import com.lanternbridge.builder.core.ui.StatCard
import com.lanternbridge.builder.domain.model.CrossingResult

@Composable
fun GameOverScreen(
    result: Screen.GameOver,
    onReplay: (Int, Int) -> Unit,
    onNext: (Int, Int) -> Unit,
    onMenu: () -> Unit
) {
    val gameOverViewModel: GameOverViewModel = viewModel(factory = AppViewModelFactory)
    val state by gameOverViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(result) {
        gameOverViewModel.open(
            CrossingResult(
                districtId = result.districtId,
                levelIndex = result.levelIndex,
                crossed = result.crossed,
                stars = result.stars,
                planksLeft = result.planksLeft,
                planksUsed = result.planksUsed
            )
        )
    }

    val overlay = if (result.crossed) {
        Brush.verticalGradient(
            listOf(
                NightRiver.copy(alpha = 0.82f),
                PlumPrimary.copy(alpha = 0.72f),
                PaperBg.copy(alpha = 0.88f)
            )
        )
    } else {
        Brush.verticalGradient(
            listOf(
                NightDeep.copy(alpha = 0.90f),
                NightRiver.copy(alpha = 0.86f),
                NightDeep.copy(alpha = 0.92f)
            )
        )
    }

    ScreenBackground(overlay = overlay) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(18.dp))
                Text(
                    text = stringResource(
                        if (result.crossed) R.string.bridge_held else R.string.cart_fell
                    ),
                    style = MaterialTheme.typography.displaySmall,
                    color = PaperLight,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(22.dp))
                StarRow(earned = state.stars, size = 56.dp)
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ResultStats(state = state)
                if (state.designUnlocked) {
                    Spacer(modifier = Modifier.height(14.dp))
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        dark = true,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(R.drawable.sprite_5),
                            contentDescription = stringResource(R.string.cd_lantern_design),
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.new_lantern),
                            style = MaterialTheme.typography.labelLarge,
                            color = LanternAmber,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(
                        when {
                            !result.crossed -> R.string.hint_fail
                            state.stars >= 3 -> R.string.hint_three_stars
                            else -> R.string.hint_two_stars
                        }
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = DustyRose,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(12.dp))
                if (result.crossed) {
                    PrimaryButton(
                        text = stringResource(R.string.next_crossing),
                        onClick = { onNext(state.nextDistrict, state.nextLevel) },
                        enabled = state.hasNext,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    SecondaryButton(
                        text = stringResource(R.string.roll_again),
                        onClick = { onReplay(result.districtId, result.levelIndex) },
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    PrimaryButton(
                        text = stringResource(R.string.roll_again),
                        onClick = { onReplay(result.districtId, result.levelIndex) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    SecondaryButton(
                        text = stringResource(R.string.next_crossing),
                        onClick = { onNext(state.nextDistrict, state.nextLevel) },
                        enabled = state.hasNext,
                        stateHint = stringResource(R.string.next_locked_state),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                TextButton(
                    onClick = onMenu,
                    modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.icon_home),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.menu),
                        style = MaterialTheme.typography.labelLarge,
                        color = PaperLight
                    )
                }
            }
        }
    }
}

@Composable
private fun ResultStats(state: GameOverUiState) {
    val cards = ArrayList<Triple<String, String, Color>>()
    if (state.planksLeft > 0) {
        cards.add(
            Triple(
                stringResource(R.string.stat_planks_left),
                state.planksLeft.toString(),
                MintAccent
            )
        )
    }
    if (state.planksUsed > 0) {
        cards.add(
            Triple(
                stringResource(R.string.stat_planks_used),
                state.planksUsed.toString(),
                PlumPrimary
            )
        )
    }
    if (state.lanternsLit > 0) {
        cards.add(
            Triple(
                stringResource(R.string.stat_lanterns),
                state.lanternsLit.toString(),
                LanternAmber
            )
        )
    }
    if (cards.size < 2) {
        cards.add(
            Triple(
                stringResource(R.string.stat_crossing),
                (state.levelIndex + 1).toString(),
                InkText
            )
        )
    }
    if (cards.size < 2) return
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        cards.forEach { card ->
            StatCard(
                label = card.first,
                value = card.second,
                valueColor = card.third,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
