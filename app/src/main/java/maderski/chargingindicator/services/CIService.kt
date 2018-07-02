package maderski.chargingindicator.services

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log
import maderski.chargingindicator.R
import maderski.chargingindicator.receivers.PowerConnectionReceiver
import maderski.chargingindicator.utils.ServiceUtils

class CIService : Service() {
    private val mPowerConnectionReceiver = PowerConnectionReceiver()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val filter = IntentFilter()
        filter.addAction("android.intent.action.ACTION_POWER_CONNECTED")
        filter.addAction("android.intent.action.ACTION_POWER_DISCONNECTED")
        registerReceiver(mPowerConnectionReceiver, filter)

        return Service.START_NOT_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "ON CREATE")
        val title = this.getString(R.string.ci_service_notification_title)
        val message = this.getString(R.string.ci_service_notification_messge)
        ServiceUtils.createServiceNotification(3449,
                title,
                message,
                this,
                "CIChannelId",
                "CIChannelName",
                R.drawable.standardbolt)
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