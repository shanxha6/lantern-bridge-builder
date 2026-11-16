package com.lanternbridge.builder.presentation.gallery

import com.lanternbridge.builder.domain.model.LanternDesign

data class GalleryUiState(
    val designs: List<LanternDesign> = emptyList(),
    val unlockedCount: Int = 0
)
