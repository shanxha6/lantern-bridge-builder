package com.lanternbridge.builder.domain.model

data class LevelSpec(
    val districtId: Int,
    val levelIndex: Int,
    val posts: List<Post>,
    val finishIndex: Int,
    val plankBudget: Int,
    val weight: Float
)
