package maderski.chargingindicator.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import maderski.chargingindicator.CIApplication
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

    init {
        CIApplication.instance.appComponent.inject(this)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val appContext = context?.applicationContext
        if (appContext != null && intent != null) {
            val pendingResult: PendingResult = goAsync()
            val isBatteryCharged = batteryHelper.isBatteryUserCharged(appContext, intent)
            val title = "Battery Level: ${batteryHelper.batteryLevel(intent)}"
            val message = if (isBatteryCharged) "CHARGED!" else "Charging..."
            val icon = if (isBatteryCharged) android.R.drawable.ic_lock_idle_charging else R.drawable.standardbolt
            Log.d(TAG, title)
            playBatteryChargedSound(isBatteryCharged)
            updateNotification(appContext, title, message, icon)
            pendingResult.finish()
        }
    }

    private fun playBatteryChargedSound(isBatteryCharged: Boolean) {
        if (isBatteryCharged && canChargedSoundPlay) {
            performActions.batteryChargedSound()
            canChargedSoundPlay = false
        }
    }

    private fun updateNotification(
        appContext: Context,
        title: String,
        message: String,
        icon: Int
    ) {
        ServiceUtils.updateServiceNotification(
            ServiceUtils.FOREGROUND_NOTIFICATION_ID,
            title,
            message,
            appContext,
            appContext.getString(R.string.ci_channel_id),
            appContext.getString(R.string.ci_channel_name),
            icon,
            true
        )
    }

    companion object {
        const val TAG = "BatteryReceiver"
    }
}