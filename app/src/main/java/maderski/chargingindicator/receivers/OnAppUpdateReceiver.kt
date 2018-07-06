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
        val isCIServiceRunning = ServiceUtils.isServiceRunning(context, CIService::class.java)
        val isJobScheduled = ServiceUtils.isJobScheduled(context)
        if (isCIServiceRunning.not() && isJobScheduled.not() && BuildConfig.DEBUG.not()) {
            intent?.let {
                val action = it.action
                if(action == Intent.ACTION_MY_PACKAGE_REPLACED) {
                    // Schedule Job to run on update
                    ServiceUtils.scheduleJob(context, CIService::class.java)
                }
            }
        }
    }

}