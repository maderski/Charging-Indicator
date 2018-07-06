package maderski.chargingindicator.services

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import android.util.Log
import maderski.chargingindicator.R
import maderski.chargingindicator.actions.PerformActions
import maderski.chargingindicator.helpers.BatteryHelper
import maderski.chargingindicator.helpers.CIBubblesHelper
import maderski.chargingindicator.receivers.BatteryReceiver
import maderski.chargingindicator.sharedprefs.CIPreferences
import maderski.chargingindicator.utils.ServiceUtils

class BatteryService : Service() {
    private val mBatteryReceiver = BatteryReceiver()

    private var mPerformActions: PerformActions? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus = registerReceiver(mBatteryReceiver, filter)

        mPerformActions = PerformActions(this)
        mPerformActions?.let {
            it.connectVibrate()
            playConnectSound(it, batteryStatus)
            it.showToast(getString(R.string.power_connected_msg))
            it.showBubble()
        }

        return START_NOT_STICKY
    }

    private fun playConnectSound(performActions: PerformActions, batteryStatus: Intent?) {
        val chargedSoundEnabled = CIPreferences.getBatteryChargedPlaySound(this)
        if(chargedSoundEnabled && batteryStatus != null) {
            val isBatteryAt100 = BatteryHelper(batteryStatus).isBatteryAt100
            if(isBatteryAt100.not()) {
                performActions.connectSound()
            }
        } else {
            performActions.connectSound()
        }
    }

    override fun onCreate() {
        super.onCreate()
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
        super.onDestroy()
        mPerformActions?.let {
            it.disconnectVibrate()
            it.disconnectSound()
            it.showToast(getString(R.string.power_disconnected_msg))
            it.removeBubble()
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val title = this.getString(R.string.ci_service_notification_title)
            val message = this.getString(R.string.ci_service_notification_messge)
            ServiceUtils.updateServiceNotification(ServiceUtils.FOREGROUND_NOTIFICATION_ID,
                    title,
                    message,
                    this,
                    getString(R.string.ci_channel_id),
                    getString(R.string.ci_channel_name),
                    R.drawable.standardbolt,
                    true)
        } else {
            stopForeground(true)
        }

        unregisterReceiver(mBatteryReceiver)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {
        const val TAG = "BatteryService"
    }
}