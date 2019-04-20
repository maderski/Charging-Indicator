package maderski.chargingindicator.helpers

import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import maderski.chargingindicator.sharedprefs.CIPreferences

import java.text.NumberFormat

/**
 * Created by Jason on 2/13/16.
 */
class BatteryHelper(private val batteryStatus: Intent) {

    // Returns true or false depending if battery is charging
    val isBatteryCharging: Boolean
        get() = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1) == BatteryManager.BATTERY_STATUS_CHARGING

    // Returns true or false depending if battery is plugged in
    val isPluggedIn: Boolean
        get() {
            val chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
            val usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB
            val acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC
            val wirelessCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_WIRELESS

            return usbCharge || acCharge || wirelessCharge
        }

    val isBatteryAt100: Boolean
        get() = batteryPercent() == 1f

    fun isBatteryUserCharged(context: Context): Boolean = (batteryPercent() * 100).toInt() == CIPreferences.getBatteryCharged(context)

    // Returns battery percentage as string
    fun batteryLevel(): String {
        val percent = batteryPercent()
        val percentFormat = NumberFormat.getPercentInstance()
        percentFormat.maximumFractionDigits = 1
        return percentFormat.format(percent.toDouble())
    }

    fun batteryPercent(): Float {
        val level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        return level / scale.toFloat()
    }

    companion object {
        private val TAG = "Battery"
    }
}
