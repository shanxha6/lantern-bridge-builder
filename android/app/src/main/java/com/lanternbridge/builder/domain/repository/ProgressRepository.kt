package com.lanternbridge.builder.domain.repository

import com.lanternbridge.builder.domain.model.DistrictProgress
import com.lanternbridge.builder.domain.model.LanternDesign

interface ProgressRepository {
    fun stars(districtId: Int, levelIndex: Int): Int
    fun saveStars(districtId: Int, levelIndex: Int, stars: Int)
    fun districts(): List<DistrictProgress>
    fun completedInDistrict(districtId: Int): Int
    fun totalStars(): Int
    fun lanternsLit(): Int
    fun isUnlocked(districtId: Int): Boolean
    fun selectedDistrict(): Int
    fun selectDistrict(districtId: Int)
    fun nextLevel(districtId: Int): Int
    fun designs(): List<LanternDesign>
    fun reset()
}
