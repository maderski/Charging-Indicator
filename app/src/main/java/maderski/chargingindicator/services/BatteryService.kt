package maderski.chargingindicator.services

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import maderski.chargingindicator.CIApplication
import maderski.chargingindicator.R
import maderski.chargingindicator.actions.CIPerformActions
import maderski.chargingindicator.actions.interfaces.PerformActions
import maderski.chargingindicator.receivers.BatteryReceiver
import maderski.chargingindicator.utils.ServiceUtils
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
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
        performActions.showToast(getString(R.string.power_connected_msg))
        performActions.showBubble()

        return START_NOT_STICKY
    }

    override fun stopService(name: Intent?): Boolean {
        stopForeground(true)
        return super.stopService(name)
    }

    override fun onCreate() {
        super.onCreate()

        CIApplication.instance.appComponent.inject(this)

        val title = getString(R.string.getting_battery_pct)
        val message = getString(R.string.getting_battery_info)
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
        doAsync {
            performActions.disconnectVibrate()
            performActions.disconnectSound()
            uiThread {
                performActions.showToast(getString(R.string.power_disconnected_msg))
                performActions.removeBubble()
                val title = it.getString(R.string.ci_service_notification_title)
                val message = it.getString(R.string.ci_service_notification_messge)
                ServiceUtils.updateServiceNotification(ServiceUtils.FOREGROUND_NOTIFICATION_ID,
                        title,
                        message,
                        it,
                        it.getString(R.string.ci_channel_id),
                        it.getString(R.string.ci_channel_name),
                        R.drawable.ic_action_battery,
                        false)
            }
        }

        unregisterReceiver(batteryReceiver)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        const val TAG = "BatteryService"
    }
}