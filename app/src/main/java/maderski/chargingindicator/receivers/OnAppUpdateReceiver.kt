package maderski.chargingindicator.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import maderski.chargingindicator.BuildConfig
import maderski.chargingindicator.services.CIService
import maderski.chargingindicator.services.StartCIService
import maderski.chargingindicator.utils.ServiceUtils

class OnAppUpdateReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null) {
            val action = intent.action
            if (action != null && action == Intent.ACTION_MY_PACKAGE_REPLACED) {
                // Schedule Job to run on update
                ServiceUtils.startService(context, CIService::class.java, CIService.TAG)
            }
        }
    }

}