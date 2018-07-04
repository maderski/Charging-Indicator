package maderski.chargingindicator.services

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log
import maderski.chargingindicator.R
import maderski.chargingindicator.helpers.CIBubblesHelper
import maderski.chargingindicator.receivers.BatteryReceiver
import maderski.chargingindicator.sharedprefs.CIPreferences
import maderski.chargingindicator.utils.ServiceUtils

class BatteryService : Service() {
    private val mBatteryReceiver = BatteryReceiver()

    private var mIsShowingFAB = false
    private var mCIBubblesHelper: CIBubblesHelper? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val filter = IntentFilter()
        registerReceiver(mBatteryReceiver, filter)
        mIsShowingFAB = CIPreferences.getShowChargingBubble(this)
        if(mIsShowingFAB) {
            mCIBubblesHelper = CIBubblesHelper(this)
            mCIBubblesHelper?.addBubble()
        }

        return START_NOT_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(CIService.TAG, "ON CREATE")
        val title = "Getting battery percent"
        val message = "Getting battery"
        ServiceUtils.createServiceNotification(3448,
                title,
                message,
                this,
                "CIChannelId",
                "CIChannelName",
                R.drawable.standardbolt)
    }

    override fun onDestroy() {
        super.onDestroy()
        if(mIsShowingFAB) {
            mCIBubblesHelper?.removeBubble()
        }

        stopForeground(true)
        unregisterReceiver(mBatteryReceiver)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {
        const val TAG = "BatteryService"
    }
}