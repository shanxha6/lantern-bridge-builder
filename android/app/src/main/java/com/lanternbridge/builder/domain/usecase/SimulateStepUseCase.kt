package com.lanternbridge.builder.domain.usecase

import com.lanternbridge.builder.core.config.GameConfig

class SimulateStepUseCase {

    fun advance(progress: Float, span: Float, deltaMs: Long): Float {
        val safeSpan = if (span < 1f) 1f else span
        return progress + GameConfig.CART_SPEED_UNITS_PER_SEC * deltaMs / 1000f / safeSpan
    }

    fun sag(span: Float, weight: Float): Float {
        val ratio = span / GameConfig.MAX_PLACE_SPAN
        return GameConfig.SAG_MAX_UNITS * ratio * ratio * weight
    }

    fun load(span: Float, weight: Float): Float = span * weight

    fun snaps(span: Float, weight: Float): Boolean = load(span, weight) > GameConfig.SNAP_LOAD

    fun stressed(span: Float, weight: Float): Boolean =
        load(span, weight) >= GameConfig.SNAP_LOAD * 0.8f
}
