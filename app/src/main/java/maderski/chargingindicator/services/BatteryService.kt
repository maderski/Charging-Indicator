package maderski.chargingindicator.services

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.widget.Toast
import maderski.chargingindicator.CIApplication
import maderski.chargingindicator.R
import maderski.chargingindicator.actions.interfaces.PerformActions
import maderski.chargingindicator.receivers.BatteryReceiver
import maderski.chargingindicator.utils.ServiceUtils
import javax.inject.Inject

class BatteryService : Service() {
    private val batteryReceiver = BatteryReceiver()

    @Inject
    lateinit var performActions: PerformActions

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus = registerReceiver(batteryReceiver, filter)
        performActions.connectVibrate()
        performActions.playConnectSound(batteryStatus)
        Toast.makeText(this, getString(R.string.power_connected_msg), Toast.LENGTH_LONG).show()
        performActions.showBubble()

        return START_NOT_STICKY
    }

    override fun stopService(name: Intent?): Boolean {
        stopForeground(false)
        return super.stopService(name)
    }

    override fun onCreate() {
        super.onCreate()

        CIApplication.instance.appComponent.inject(this)

        val title = getString(R.string.getting_battery_pct)
        val message = getString(R.string.getting_battery_info)
        ServiceUtils.createServiceNotification(
            ServiceUtils.FOREGROUND_NOTIFICATION_ID,
            title,
            message,
            this,
            getString(R.string.ci_channel_id),
            getString(R.string.ci_channel_name),
            R.drawable.standardbolt,
            true
        )
    }

    override fun onDestroy() {
        unregisterReceiver(batteryReceiver)

        performActions.disconnectVibrate()
        performActions.disconnectSound()

        Toast.makeText(this, getString(R.string.power_disconnected_msg), Toast.LENGTH_LONG).show()
        performActions.removeBubble()
        val title = getString(R.string.ci_service_notification_title)
        val message = getString(R.string.ci_service_notification_messge)
        ServiceUtils.updateServiceNotification(
            ServiceUtils.FOREGROUND_NOTIFICATION_ID,
            title,
            message,
            this,
            getString(R.string.ci_channel_id),
            getString(R.string.ci_channel_name),
            R.drawable.ic_action_battery,
            false
        )

        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        const val TAG = "BatteryService"
    }
}