package maderski.chargingindicator.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import maderski.chargingindicator.R
import maderski.chargingindicator.serviceconnection.CIServiceConnection
import maderski.chargingindicator.services.BatteryService
import maderski.chargingindicator.services.CIService
import maderski.chargingindicator.utils.PowerUtils
import maderski.chargingindicator.utils.ServiceUtils

class OnBootWorker(context: Context, workerParms: WorkerParameters) : Worker(context, workerParms) {

    override fun doWork(): Result {
        val isPowerConnected = PowerUtils.isPluggedIn(applicationContext)
        ServiceUtils.startBindedService(applicationContext, CIService::class.java, CIService.TAG, CIServiceConnection {
            if (isPowerConnected) {
                ServiceUtils.startService(applicationContext, BatteryService::class.java, BatteryService.TAG)
            } else {
                with(applicationContext) {
                    val title = getString(R.string.ci_service_notification_title)
                    val message = getString(R.string.ci_service_notification_messge)
                    ServiceUtils.updateServiceNotification(ServiceUtils.FOREGROUND_NOTIFICATION_ID,
                            title,
                            message,
                            this,
                            getString(R.string.ci_channel_id),
                            getString(R.string.ci_channel_name),
                            R.drawable.ic_action_battery,
                            false)
                }
            }
        })
        return Result.success()
    }

    companion object {
        const val TAG = "OnBootWorker"
    }
}