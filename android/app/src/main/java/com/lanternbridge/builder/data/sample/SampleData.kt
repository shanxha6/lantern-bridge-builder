package com.lanternbridge.builder.data.sample

import com.lanternbridge.builder.core.config.GameConfig
import com.lanternbridge.builder.domain.model.LanternDesign
import com.lanternbridge.builder.domain.model.LevelSpec
import com.lanternbridge.builder.domain.model.Post

object SampleData {

    private val CHAIN_POSTS = intArrayOf(5, 6, 6, 7, 8)
    private val DRIFT = floatArrayOf(90f, 85f, 80f, 80f, 75f)
    private const val START_X = 80f
    private const val END_X = 920f
    private const val MID_Y = 300f

    val lanternDesigns: List<LanternDesign> = buildDesigns()

    fun level(districtId: Int, levelIndex: Int): LevelSpec {
        val district = districtId.coerceIn(1, GameConfig.DISTRICT_COUNT)
        val index = levelIndex.coerceIn(0, GameConfig.LEVELS_PER_DISTRICT - 1)
        val chain = CHAIN_POSTS[district - 1]
        val drift = DRIFT[district - 1]
        val random = Lcg(district * 7919 + index * 193 + 41)
        val posts = ArrayList<Post>()
        val step = (END_X - START_X) / (chain + 1)
        posts.add(Post(START_X, MID_Y))
        var previousY = MID_Y
        for (slot in 1..chain) {
            val delta = (random.next() - 0.5f) * 2f * drift
            val y = (previousY + delta).coerceIn(MID_Y - drift, MID_Y + drift)
            val jitter = (random.next() - 0.5f) * step * 0.2f
            posts.add(Post(START_X + step * slot + jitter, y))
            previousY = y
        }
        posts.add(Post(END_X, MID_Y))
        val finishIndex = posts.size - 1
        val decoys = 2 + index % 3
        for (slot in 0 until decoys) {
            val along = 0.18f + random.next() * 0.64f
            val x = START_X + (END_X - START_X) * along
            val high = random.next() < 0.5f
            val offset = 170f + random.next() * 90f
            val y = if (high) MID_Y - offset else MID_Y + offset
            posts.add(Post(x, y.coerceIn(70f, 530f)))
        }
        return LevelSpec(
            districtId = district,
            levelIndex = index,
            posts = posts,
            finishIndex = finishIndex,
            plankBudget = chain + 3 + index % 3,
            weight = GameConfig.WEIGHT_BY_DISTRICT[district - 1]
        )
    }

    private fun buildDesigns(): List<LanternDesign> {
        val designs = ArrayList<LanternDesign>()
        var id = 0
        for (district in 1..GameConfig.DISTRICT_COUNT) {
            designs.add(LanternDesign(id, district, 8, false))
            id += 1
            designs.add(LanternDesign(id, district, GameConfig.LEVELS_PER_DISTRICT, false))
            id += 1
        }
        return designs
    }

    private class Lcg(seed: Int) {
        private var state: Int = seed * 2654435761.toInt() + 12345

        fun next(): Float {
            state = state * 1664525 + 1013904223
            val bits = (state ushr 9) and 0x7FFF
            return bits.toFloat() / 32767f
        }
    }
}
