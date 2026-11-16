package com.lanternbridge.builder.domain.model

data class DistrictProgress(
    val id: Int,
    val completed: Int,
    val total: Int,
    val stars: Int,
    val unlocked: Boolean
)
