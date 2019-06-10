package maderski.chargingindicator.helpers.battery

import android.content.Context
import android.content.Intent

interface BatteryHelper {
    // Returns true or false depending if battery is charging
    fun isBatteryCharging(batteryStatus: Intent): Boolean
    // Returns true or false depending if battery is plugged in
    fun isPluggedIn(batteryStatus: Intent): Boolean
    fun isBatteryAt100(batteryStatus: Intent): Boolean

    fun isBatteryUserCharged(context: Context, batteryStatus: Intent): Boolean
    // Returns battery percentage as string
    fun batteryLevel(batteryStatus: Intent): String

    fun batteryPercent(batteryStatus: Intent): Float
}