package com.lanternbridge.builder.presentation.menu

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lanternbridge.builder.R
import com.lanternbridge.builder.core.config.GameConfig
import com.lanternbridge.builder.core.di.AppViewModelFactory
import com.lanternbridge.builder.core.theme.DustyRose
import com.lanternbridge.builder.core.theme.InkMuted
import com.lanternbridge.builder.core.theme.InkText
import com.lanternbridge.builder.core.theme.LanternAmber
import com.lanternbridge.builder.core.theme.PaperBg
import com.lanternbridge.builder.core.theme.PaperSurfaceAlt
import com.lanternbridge.builder.core.theme.PlumPrimary
import com.lanternbridge.builder.core.ui.GlassCard
import com.lanternbridge.builder.core.ui.Labels
import com.lanternbridge.builder.core.ui.PrimaryButton
import com.lanternbridge.builder.core.ui.ScreenBackground
import com.lanternbridge.builder.core.ui.StatCard

@Composable
fun MenuScreen(
    onPlay: (Int, Int) -> Unit,
    onGallery: () -> Unit,
    onTutorial: () -> Unit,
    onSettings: () -> Unit
) {
    val menuViewModel: MenuViewModel = viewModel(factory = AppViewModelFactory)
    val state by menuViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        menuViewModel.refresh()
    }

    ScreenBackground(
        overlay = Brush.verticalGradient(
            listOf(
                PaperBg.copy(alpha = 0.95f),
                PaperSurfaceAlt.copy(alpha = 0.97f),
                DustyRose.copy(alpha = 0.55f)
            )
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 18.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.sprite_3),
                    contentDescription = stringResource(R.string.cd_logo),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = stringResource(R.string.title_main),
                    style = MaterialTheme.typography.titleMedium,
                    color = InkText,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onSettings, modifier = Modifier.size(48.dp)) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = stringResource(R.string.cd_settings),
                        tint = PlumPrimary,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard(
                    label = stringResource(R.string.stat_lanterns),
                    value = stringResource(
                        R.string.fraction,
                        state.lanterns,
                        GameConfig.TOTAL_CROSSINGS
                    ),
                    valueColor = LanternAmber,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    label = stringResource(R.string.stat_stars),
                    value = stringResource(
                        R.string.fraction,
                        state.stars,
                        GameConfig.TOTAL_STARS
                    ),
                    valueColor = PlumPrimary,
                    modifier = Modifier.weight(1f)
                )
            }

            AnimatedVisibility(
                visible = true,
                enter = fadeIn(animationSpec = tween(durationMillis = 260)) +
                    slideInVertically(animationSpec = tween(durationMillis = 260)) { full ->
                        full / 6
                    },
                modifier = Modifier.weight(1f)
            ) {
                GlassCard(
                    modifier = Modifier.fillMaxSize(),
                    dark = false,
                    radius = 18.dp,
                    innerPadding = 18.dp
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(R.drawable.sprite_3),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stringResource(Labels.districtName(state.selectedDistrict)),
                                style = MaterialTheme.typography.headlineSmall,
                                color = InkText
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = stringResource(
                                    R.string.crossing_label,
                                    state.selectedDistrict,
                                    state.nextLevel + 1
                                ),
                                style = MaterialTheme.typography.labelLarge,
                                color = InkMuted
                            )
                        }
                        Image(
                            painter = painterResource(R.drawable.sprite_1),
                            contentDescription = stringResource(R.string.cd_cart),
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(72.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = stringResource(R.string.menu_hint),
                        style = MaterialTheme.typography.bodyMedium,
                        color = InkMuted
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    PrimaryButton(
                        text = stringResource(R.string.play),
                        onClick = { onPlay(state.selectedDistrict, state.nextLevel) },
                        leadingIcon = Icons.Default.PlayArrow,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                state.districts.forEach { district ->
                    DistrictTile(
                        district = district,
                        selected = district.id == state.selectedDistrict,
                        onClick = { menuViewModel.selectDistrict(district.id) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MenuBentoButton(
                    label = stringResource(R.string.lantern_gallery),
                    onClick = onGallery,
                    modifier = Modifier.weight(1f)
                ) {
                    Image(
                        painter = painterResource(R.drawable.sprite_5),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(34.dp)
                    )
                }
                MenuBentoButton(
                    label = stringResource(R.string.how_to_build),
                    onClick = onTutorial,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = PlumPrimary,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun MenuBentoButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit
) {
    val shape = RoundedCornerShape(16.dp)
    OutlinedButton(
        onClick = onClick,
        shape = shape,
        border = BorderStroke(width = 1.5.dp, color = InkText.copy(alpha = 0.20f)),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = InkText
        ),
        modifier = modifier
            .height(92.dp)
            .background(color = PaperSurfaceAlt.copy(alpha = 0.88f), shape = shape)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            icon()
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = InkText
            )
        }
    }
}
