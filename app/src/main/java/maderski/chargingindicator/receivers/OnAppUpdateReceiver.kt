package maderski.chargingindicator.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import maderski.chargingindicator.services.StartCIService
import maderski.chargingindicator.utils.ServiceUtils

class OnAppUpdateReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // Schedule Job to launch CIService on boot
        //ServiceUtils.scheduleJob(context, StartCIService::class.java)
    }

}