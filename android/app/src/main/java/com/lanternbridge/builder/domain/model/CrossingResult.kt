package com.lanternbridge.builder.domain.model

data class CrossingResult(
    val districtId: Int,
    val levelIndex: Int,
    val crossed: Boolean,
    val stars: Int,
    val planksLeft: Int,
    val planksUsed: Int
)
