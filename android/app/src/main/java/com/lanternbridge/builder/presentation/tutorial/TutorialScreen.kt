package com.lanternbridge.builder.presentation.tutorial

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lanternbridge.builder.R
import com.lanternbridge.builder.core.di.AppViewModelFactory
import com.lanternbridge.builder.core.theme.NightDeep
import com.lanternbridge.builder.core.theme.NightRiver
import com.lanternbridge.builder.core.theme.PaperLight
import com.lanternbridge.builder.core.ui.PrimaryButton
import com.lanternbridge.builder.core.ui.ScreenBackground
import com.lanternbridge.builder.core.ui.ScreenHeader

@Composable
fun TutorialScreen(
    onBack: () -> Unit,
    onStart: () -> Unit
) {
    val tutorialViewModel: TutorialViewModel = viewModel(factory = AppViewModelFactory)
    val steps by tutorialViewModel.steps.collectAsStateWithLifecycle()

    ScreenBackground(
        overlay = Brush.verticalGradient(
            listOf(
                NightDeep.copy(alpha = 0.80f),
                NightRiver.copy(alpha = 0.84f),
                NightDeep.copy(alpha = 0.92f)
            )
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 16.dp)
        ) {
            ScreenHeader(
                title = stringResource(R.string.how_to_build),
                subtitle = stringResource(R.string.tutorial_subtitle),
                titleColor = PaperLight,
                onBack = onBack,
                backLabel = stringResource(R.string.cd_menu)
            )
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f),
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                itemsIndexed(items = steps) { index, step ->
                    TutorialCard(
                        step = step,
                        index = index,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            PrimaryButton(
                text = stringResource(R.string.start_building),
                onClick = onStart,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
