package com.lanternbridge.builder.presentation.menu

import com.lanternbridge.builder.domain.model.DistrictProgress

data class MenuUiState(
    val lanterns: Int = 0,
    val stars: Int = 0,
    val districts: List<DistrictProgress> = emptyList(),
    val selectedDistrict: Int = 1,
    val nextLevel: Int = 0
)
