package maderski.chargingindicator.receivers

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import maderski.chargingindicator.actions.PerformActions
import maderski.chargingindicator.helpers.BatteryHelper

class BatteryReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            val action = it.action
            if(action != null && context != null) {
                val performActions = PerformActions(context)
                val batteryHelper = BatteryHelper(it)
                val isBatteryAt100 = batteryHelper.isBatteryAt100
                if(isBatteryAt100) {
                    performActions.batteryChargedSound()
                }

                Log.d(TAG, "Battery Level: ${batteryHelper.batteryLevel()}")
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