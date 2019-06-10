package maderski.chargingindicator.helpers.battery

import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import maderski.chargingindicator.sharedprefs.CIPreferences

import java.text.NumberFormat

/**
 * Created by Jason on 2/13/16.
 */
class CIBatteryHelper : BatteryHelper {

    // Returns true or false depending if battery is charging
    override fun isBatteryCharging(batteryStatus: Intent): Boolean {
        return batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1) == BatteryManager.BATTERY_STATUS_CHARGING
    }

    // Returns true or false depending if battery is plugged in
    override fun isPluggedIn(batteryStatus: Intent): Boolean {
            val chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
            val usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB
            val acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC
            val wirelessCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_WIRELESS

            return usbCharge || acCharge || wirelessCharge
        }

    override fun isBatteryAt100(batteryStatus: Intent): Boolean = batteryPercent(batteryStatus) == 1f

    override fun isBatteryUserCharged(context: Context, batteryStatus: Intent): Boolean = (batteryPercent(batteryStatus) * 100).toInt() == CIPreferences.getBatteryCharged(context)

    // Returns battery percentage as string
    override fun batteryLevel(batteryStatus: Intent): String {
        val percent = batteryPercent(batteryStatus)
        val percentFormat = NumberFormat.getPercentInstance()
        percentFormat.maximumFractionDigits = 1
        return percentFormat.format(percent.toDouble())
    }

    override fun batteryPercent(batteryStatus: Intent): Float {
        val level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        return level / scale.toFloat()
    }
}
