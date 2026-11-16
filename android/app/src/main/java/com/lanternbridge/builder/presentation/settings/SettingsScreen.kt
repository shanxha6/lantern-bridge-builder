package com.lanternbridge.builder.presentation.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
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
import com.lanternbridge.builder.core.ui.ScreenBackground
import com.lanternbridge.builder.core.ui.ScreenHeader
import com.lanternbridge.builder.core.ui.SecondaryButton
import com.lanternbridge.builder.core.ui.StatCard
import com.lanternbridge.builder.presentation.dialog.ConfirmResetDialog

@Composable
fun SettingsScreen(onBack: () -> Unit) {
    val settingsViewModel: SettingsViewModel = viewModel(factory = AppViewModelFactory)
    val state by settingsViewModel.state.collectAsStateWithLifecycle()

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
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ScreenHeader(
                title = stringResource(R.string.settings),
                subtitle = stringResource(R.string.settings_subtitle),
                titleColor = InkText,
                onBack = onBack,
                backLabel = stringResource(R.string.cd_menu)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
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

            SettingRow(
                label = stringResource(R.string.setting_sound),
                hint = stringResource(R.string.setting_sound_hint),
                checked = state.sound,
                onCheckedChange = { value -> settingsViewModel.setSound(value) }
            )
            SettingRow(
                label = stringResource(R.string.setting_vibration),
                hint = stringResource(R.string.setting_vibration_hint),
                checked = state.vibration,
                onCheckedChange = { value -> settingsViewModel.setVibration(value) }
            )
            SettingRow(
                label = stringResource(R.string.setting_gentle),
                hint = stringResource(R.string.setting_gentle_hint),
                checked = state.gentleMode,
                onCheckedChange = { value -> settingsViewModel.setGentleMode(value) }
            )

            Spacer(modifier = Modifier.weight(1f))

            SecondaryButton(
                text = stringResource(R.string.reset_progress),
                onClick = { settingsViewModel.askReset() },
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = stringResource(R.string.version_line),
                style = MaterialTheme.typography.labelSmall,
                color = InkMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    if (state.showResetDialog) {
        ConfirmResetDialog(
            onCancel = { settingsViewModel.dismissReset() },
            onConfirm = { settingsViewModel.confirmReset() }
        )
    }
}
