package maderski.chargingindicator.receivers

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import maderski.chargingindicator.R
import maderski.chargingindicator.actions.PerformActions
import maderski.chargingindicator.helpers.BatteryHelper
import maderski.chargingindicator.utils.ServiceUtils

class BatteryReceiver : BroadcastReceiver() {
    private var canChargedSoundPlay = true

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            val action = it.action
            if(action != null && context != null) {
                val performActions = PerformActions(context)
                val batteryHelper = BatteryHelper(it)
                val isBatteryAt100 = batteryHelper.isBatteryAt100
                if (isBatteryAt100 && canChargedSoundPlay) {
                    performActions.batteryChargedSound()
                    canChargedSoundPlay = false
                }

                val title = "Battery Level: ${batteryHelper.batteryLevel()}"
                val message = if (isBatteryAt100) "CHARGED!" else "Charging..."

                Log.d(TAG, title)
                ServiceUtils.updateServiceNotification(ServiceUtils.FOREGROUND_NOTIFICATION_ID,
                        title,
                        message,
                        context,
                        context.getString(R.string.ci_channel_id),
                        context.getString(R.string.ci_channel_name),
                        R.drawable.standardbolt,
                        true)
            }
        }
    }

    companion object {
        const val TAG = "BatteryReceiver"
    }

    private fun updateNotification(context: Context, title: String) {

        val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    }
}