package com.lanternbridge.builder.core.ui

import com.lanternbridge.builder.R

object Labels {

    private val DISTRICT_NAMES = intArrayOf(
        R.string.district_1,
        R.string.district_2,
        R.string.district_3,
        R.string.district_4,
        R.string.district_5
    )

    private val LANTERN_NAMES = intArrayOf(
        R.string.lantern_1,
        R.string.lantern_2,
        R.string.lantern_3,
        R.string.lantern_4,
        R.string.lantern_5,
        R.string.lantern_6,
        R.string.lantern_7,
        R.string.lantern_8,
        R.string.lantern_9,
        R.string.lantern_10
    )

    fun districtName(districtId: Int): Int =
        DISTRICT_NAMES[(districtId - 1).coerceIn(0, DISTRICT_NAMES.size - 1)]

    fun lanternName(designId: Int): Int =
        LANTERN_NAMES[designId.coerceIn(0, LANTERN_NAMES.size - 1)]
}
