package com.lanternbridge.builder.domain.model

data class LanternDesign(
    val id: Int,
    val districtId: Int,
    val unlockCrossing: Int,
    val unlocked: Boolean
)
