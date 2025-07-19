package com.hamham.gpsmover.xposed

import com.hamham.gpsmover.BuildConfig
import de.robv.android.xposed.XSharedPreferences
import de.robv.android.xposed.XposedBridge

class Xshare {

    private var xPref: XSharedPreferences = XSharedPreferences(BuildConfig.APPLICATION_ID, "${BuildConfig.APPLICATION_ID}_prefs")

    init {
        try {
            xPref.makeWorldReadable()
        } catch (_: Throwable) {
        }
    }

    private fun reloadPrefs() {
        xPref.reload()
    }

    val isStarted: Boolean
        get() {
            reloadPrefs()
            val started = xPref.getBoolean("start", false)
            XposedBridge.log("Xposed: isStarted = $started")
            return started
        }

    val getLat: Double
        get() {
            reloadPrefs()
            val lat = xPref.getFloat("latitude", 22.2855200f).toDouble()
            XposedBridge.log("Xposed: getLat = $lat")
            return lat
        }

    val getLng: Double
        get() {
            reloadPrefs()
            val lng = xPref.getFloat("longitude", 114.1576900f).toDouble()
            XposedBridge.log("Xposed: getLng = $lng")
            return lng
        }

    val isHookedSystem: Boolean
        get() {
            reloadPrefs()
            return xPref.getBoolean("isHookedSystem", false)
        }

    val randomPositionRange: Double
        get() {
            reloadPrefs()
            return xPref.getString("random_position_range", "0")?.toDoubleOrNull() ?: 0.0
        }

    val isRandomPositionEnabled: Boolean
        get() = randomPositionRange > 0.0

    val accuracyValue: Float
        get() {
            reloadPrefs()
            return xPref.getString("accuracy_settings", "5")?.toFloatOrNull() ?: 5.0f
        }

    val isAccuracyEnabled: Boolean
        get() = accuracyValue > 0.0f
}
