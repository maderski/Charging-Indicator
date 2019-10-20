package maderski.chargingindicator.services

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.Binder
import android.os.Build
import android.os.IBinder
import maderski.chargingindicator.R
import maderski.chargingindicator.receivers.PowerConnectionReceiver
import maderski.chargingindicator.utils.ServiceUtils

class CIService : Service() {
    private val cIBinder = CIBinder()
    private val powerConnectionReceiver = PowerConnectionReceiver()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val intentFilter = IntentFilter()
        intentFilter.addAction("android.intent.action.ACTION_POWER_CONNECTED")
        intentFilter.addAction("android.intent.action.ACTION_POWER_DISCONNECTED")

        registerReceiver(powerConnectionReceiver, intentFilter)

        return START_STICKY
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
                R.drawable.ic_action_battery,
                false)

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(powerConnectionReceiver)
    }

    override fun onBind(intent: Intent?): IBinder? = cIBinder

    inner class CIBinder : Binder() {
        fun getService(): CIService = this@CIService
    }

    companion object {
        const val TAG = "CIService"
    }

}