package com.lanternbridge.builder.data.repository

import com.lanternbridge.builder.core.config.GameConfig
import com.lanternbridge.builder.data.local.LanternPrefs
import com.lanternbridge.builder.data.sample.SampleData
import com.lanternbridge.builder.domain.model.DistrictProgress
import com.lanternbridge.builder.domain.model.LanternDesign
import com.lanternbridge.builder.domain.repository.ProgressRepository

class ProgressRepositoryImpl(private val prefs: LanternPrefs) : ProgressRepository {

    override fun stars(districtId: Int, levelIndex: Int): Int =
        prefs.readInt(LanternPrefs.starsKey(districtId, levelIndex), 0)

    override fun saveStars(districtId: Int, levelIndex: Int, stars: Int) {
        val key = LanternPrefs.starsKey(districtId, levelIndex)
        val best = prefs.readInt(key, 0)
        if (stars > best) prefs.writeInt(key, stars)
    }

    override fun districts(): List<DistrictProgress> {
        val result = ArrayList<DistrictProgress>()
        for (id in 1..GameConfig.DISTRICT_COUNT) {
            result.add(
                DistrictProgress(
                    id = id,
                    completed = completedInDistrict(id),
                    total = GameConfig.LEVELS_PER_DISTRICT,
                    stars = starsInDistrict(id),
                    unlocked = isUnlocked(id)
                )
            )
        }
        return result
    }

    override fun completedInDistrict(districtId: Int): Int {
        var done = 0
        for (level in 0 until GameConfig.LEVELS_PER_DISTRICT) {
            if (stars(districtId, level) > 0) done += 1
        }
        return done
    }

    override fun totalStars(): Int {
        var total = 0
        for (id in 1..GameConfig.DISTRICT_COUNT) total += starsInDistrict(id)
        return total
    }

    override fun lanternsLit(): Int {
        var total = 0
        for (id in 1..GameConfig.DISTRICT_COUNT) total += completedInDistrict(id)
        return total
    }

    override fun isUnlocked(districtId: Int): Boolean {
        if (districtId <= 1) return true
        return completedInDistrict(districtId - 1) >= GameConfig.DISTRICT_UNLOCK_CROSSINGS
    }

    override fun selectedDistrict(): Int {
        val stored = prefs.readInt(LanternPrefs.KEY_SELECTED_DISTRICT, 1)
        val safe = stored.coerceIn(1, GameConfig.DISTRICT_COUNT)
        return if (isUnlocked(safe)) safe else 1
    }

    override fun selectDistrict(districtId: Int) {
        if (isUnlocked(districtId)) {
            prefs.writeInt(LanternPrefs.KEY_SELECTED_DISTRICT, districtId)
        }
    }

    override fun nextLevel(districtId: Int): Int {
        for (level in 0 until GameConfig.LEVELS_PER_DISTRICT) {
            if (stars(districtId, level) == 0) return level
        }
        return 0
    }

    override fun designs(): List<LanternDesign> = SampleData.lanternDesigns.map { design ->
        design.copy(unlocked = completedInDistrict(design.districtId) >= design.unlockCrossing)
    }

    override fun reset() {
        prefs.clearAll()
    }

    private fun starsInDistrict(districtId: Int): Int {
        var total = 0
        for (level in 0 until GameConfig.LEVELS_PER_DISTRICT) total += stars(districtId, level)
        return total
    }
}
