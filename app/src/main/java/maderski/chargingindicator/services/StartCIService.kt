package maderski.chargingindicator.services

import android.app.job.JobParameters
import android.app.job.JobService
import maderski.chargingindicator.utils.ServiceUtils

class StartCIService : JobService() {
    override fun onStartJob(params: JobParameters?): Boolean {
        ServiceUtils.startService(applicationContext, CIService::class.java, CIService.TAG)
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return true
    }
}