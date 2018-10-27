package maderski.chargingindicator.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import maderski.chargingindicator.R
import maderski.chargingindicator.actions.PerformActions
import maderski.chargingindicator.services.BatteryService
import maderski.chargingindicator.utils.ServiceUtils


class PowerConnectionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null) {
            val action = intent.action
            action?.let {
                when(it) {
                    Intent.ACTION_POWER_CONNECTED -> {
                        ServiceUtils.startService(context, BatteryService::class.java, BatteryService.TAG)
                    }
                    Intent.ACTION_POWER_DISCONNECTED -> {
                        ServiceUtils.stopService(context, BatteryService::class.java, BatteryService.TAG)
                    }
                }
            }
        }
    }

}