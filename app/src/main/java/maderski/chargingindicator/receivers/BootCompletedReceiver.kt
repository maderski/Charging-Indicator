package maderski.chargingindicator.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import maderski.chargingindicator.services.StartCIService
import maderski.chargingindicator.utils.ServiceUtils

class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null) {
            val action = intent.action
            if(action == Intent.ACTION_BOOT_COMPLETED) {
                // Schedule Job to launch CIService on boot
                ServiceUtils.scheduleJob(context, StartCIService::class.java)
            }
        }
    }
}