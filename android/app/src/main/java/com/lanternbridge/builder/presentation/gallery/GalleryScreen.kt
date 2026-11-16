package com.lanternbridge.builder.presentation.gallery

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
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
import com.lanternbridge.builder.core.theme.PaperBg
import com.lanternbridge.builder.core.theme.PaperSurfaceAlt
import com.lanternbridge.builder.core.ui.ScreenBackground
import com.lanternbridge.builder.core.ui.ScreenHeader

@Composable
fun GalleryScreen(onBack: () -> Unit) {
    val galleryViewModel: GalleryViewModel = viewModel(factory = AppViewModelFactory)
    val state by galleryViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        galleryViewModel.refresh()
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
                .padding(horizontal = 16.dp)
        ) {
            ScreenHeader(
                title = stringResource(R.string.lantern_gallery),
                subtitle = stringResource(
                    R.string.fraction,
                    state.unlockedCount,
                    GameConfig.LANTERN_DESIGNS
                ),
                titleColor = InkText,
                onBack = onBack,
                backLabel = stringResource(R.string.cd_menu)
            )
            Text(
                text = stringResource(R.string.gallery_caption),
                style = MaterialTheme.typography.bodyMedium,
                color = InkMuted,
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxWidth().weight(1f)
            ) {
                items(items = state.designs, key = { design -> design.id }) { design ->
                    LanternCard(design = design, modifier = Modifier.fillMaxWidth())
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}
