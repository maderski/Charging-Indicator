package maderski.chargingindicator.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import maderski.chargingindicator.R
import maderski.chargingindicator.actions.interfaces.PerformActions
import maderski.chargingindicator.helpers.battery.BatteryHelper
import maderski.chargingindicator.utils.ServiceUtils
import javax.inject.Inject

class BatteryReceiver : BroadcastReceiver() {
    @Inject
    lateinit var performActions: PerformActions

    @Inject
    lateinit var batteryHelper: BatteryHelper

    private var canChargedSoundPlay = true

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            val action = it.action
            if (action != null && context != null) {
                val appContext = context.applicationContext
                val isBatteryCharged = batteryHelper.isBatteryUserCharged(appContext, intent)
                if (isBatteryCharged && canChargedSoundPlay) {
                    performActions.batteryChargedSound()
                    canChargedSoundPlay = false
                }

                val title = "Battery Level: ${batteryHelper.batteryLevel(intent)}"
                val message = if (isBatteryCharged) "CHARGED!" else "Charging..."
                val icon = if (isBatteryCharged) android.R.drawable.ic_lock_idle_charging else R.drawable.standardbolt

                Log.d(TAG, title)
                ServiceUtils.updateServiceNotification(ServiceUtils.FOREGROUND_NOTIFICATION_ID,
                        title,
                        message,
                        context,
                        context.getString(R.string.ci_channel_id),
                        context.getString(R.string.ci_channel_name),
                        icon,
                        true)
            }
        }
    }

    companion object {
        const val TAG = "BatteryReceiver"
    }
}