package maderski.chargingindicator.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import maderski.chargingindicator.services.BatteryService
import maderski.chargingindicator.services.CIService
import maderski.chargingindicator.utils.ServiceUtils

class OnBootWorker(context: Context, workerParms: WorkerParameters) : Worker(context, workerParms) {
    override fun doWork(): Result {
        ServiceUtils.startService(applicationContext, CIService::class.java, CIService.TAG)
        ServiceUtils.startService(applicationContext, BatteryService::class.java, BatteryService.TAG)
        return Result.success()
    }

    companion object {
        const val TAG = "OnBootWorker"
    }
}