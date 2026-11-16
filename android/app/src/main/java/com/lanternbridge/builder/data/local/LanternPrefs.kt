package com.lanternbridge.builder.data.local

import android.content.Context
import android.content.SharedPreferences

class LanternPrefs(context: Context) {

    private val store: SharedPreferences =
        context.getSharedPreferences(STORE_NAME, Context.MODE_PRIVATE)

    fun readInt(key: String, fallback: Int): Int = store.getInt(key, fallback)

    fun writeInt(key: String, value: Int) {
        store.edit().putInt(key, value).apply()
    }

    fun readBoolean(key: String, fallback: Boolean): Boolean = store.getBoolean(key, fallback)

    fun writeBoolean(key: String, value: Boolean) {
        store.edit().putBoolean(key, value).apply()
    }

    fun clearAll() {
        store.edit().clear().apply()
    }

    companion object {
        private const val STORE_NAME = "lantern_bridge_builder"
        const val KEY_SELECTED_DISTRICT = "selected_district"
        const val KEY_SOUND = "sound_enabled"
        const val KEY_VIBRATION = "vibration_enabled"
        const val KEY_GENTLE = "gentle_mode"

        fun starsKey(districtId: Int, levelIndex: Int): String = "stars_" + districtId + "_" + levelIndex
    }
}
