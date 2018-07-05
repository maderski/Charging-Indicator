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
        intent?.let {
            val action = it.action
            if(action != null && context != null) {
                when(action) {
                    Intent.ACTION_POWER_CONNECTED -> {
                        ServiceUtils.startService(context, BatteryService::class.java, BatteryService.TAG)
                    }
                    Intent.ACTION_POWER_DISCONNECTED -> {
                        val isServiceRunning = ServiceUtils.isServiceRunning(context, BatteryService::class.java)
                        if (isServiceRunning) {
                            ServiceUtils.stopService(context, BatteryService::class.java, BatteryService.TAG)
                        }

                    }
                }
            }
        }
    }

}