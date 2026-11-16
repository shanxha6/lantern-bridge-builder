package com.lanternbridge.builder.core.config

object GameConfig {
    const val LOADER_DURATION_MS = 8000L
    const val DISTRICT_COUNT = 5
    const val LEVELS_PER_DISTRICT = 16
    const val TOTAL_CROSSINGS = 80
    const val TOTAL_STARS = 240
    const val LANTERN_DESIGNS = 10
    const val BOARD_W = 1000f
    const val BOARD_H = 600f
    const val MAX_PLACE_SPAN = 280f
    const val SNAP_LOAD = 240f
    const val SAG_MAX_UNITS = 34f
    const val POST_HIT_RADIUS_UNITS = 46f
    const val CART_SPEED_UNITS_PER_SEC = 210f
    const val TICK_MS = 16L
    const val STAR3_RATIO = 0.40f
    const val STAR2_RATIO = 0.20f
    const val DISTRICT_UNLOCK_CROSSINGS = 12
    const val SNAP_FALL_MS = 520L
    const val WIN_HANDOFF_MS = 700L
    const val CART_LIFT_UNITS = 26f
    const val BANK_WIDTH_UNITS = 90f
    val WEIGHT_BY_DISTRICT = floatArrayOf(1.00f, 1.08f, 1.18f, 1.28f, 1.40f)
}
