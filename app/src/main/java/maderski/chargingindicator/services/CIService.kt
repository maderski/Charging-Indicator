package maderski.chargingindicator.services

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import maderski.chargingindicator.BuildConfig
import maderski.chargingindicator.R
import maderski.chargingindicator.helpers.BatteryHelper
import maderski.chargingindicator.receivers.BatteryReceiver
import maderski.chargingindicator.receivers.PowerConnectionReceiver
import maderski.chargingindicator.utils.ServiceUtils

class CIService : Service() {
    private val mPowerConnectionReceiver = PowerConnectionReceiver()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        IntentFilter(Intent.ACTION_BATTERY_CHANGED).let {
            intentFilter -> registerReceiver(mPowerConnectionReceiver, intentFilter)
        }

        stopForeground(true)
        stopSelf()

        return Service.START_NOT_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        val title = this.getString(R.string.ci_service_notification_title)
        val message = this.getString(R.string.ci_service_notification_messge)
        ServiceUtils.createServiceNotification(ServiceUtils.FOREGROUND_NOTIFICATION_ID,
                title,
                message,
                this,
                getString(R.string.ci_channel_id),
                getString(R.string.ci_channel_name),
                R.drawable.standardbolt,
                true)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mPowerConnectionReceiver)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {
        const val TAG = "CIService"
    }

}