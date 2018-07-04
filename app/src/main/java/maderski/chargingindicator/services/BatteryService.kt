package maderski.chargingindicator.services

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import com.txusballesteros.bubbles.BubblesManager
import maderski.chargingindicator.R
import maderski.chargingindicator.interfaces.LastXYCoordListener
import maderski.chargingindicator.receivers.BatteryReceiver
import maderski.chargingindicator.sharedprefs.CIPreferences
import maderski.chargingindicator.ui.custom.CustomBubbleLayout
import maderski.chargingindicator.utils.ServiceUtils

class BatteryService : Service() {
    private val mBatteryReceiver = BatteryReceiver()

    private var mIsShowingFAB = false
    private var mBubblesManager: BubblesManager? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val filter = IntentFilter()
        registerReceiver(mBatteryReceiver, filter)
        mIsShowingFAB = CIPreferences.getShowChargingBubble(this)
        if(mIsShowingFAB) {
            mBubblesManager = BubblesManager.Builder(this)
                    .setInitializationCallback {
                        val bubbleView: CustomBubbleLayout = LayoutInflater.from(this).inflate(R.layout.floating_widget_layout, null) as CustomBubbleLayout
                        bubbleView.setShouldStickToWall(false)
                        bubbleView.setLastXYCoordListener(object: LastXYCoordListener{
                            override fun onLastXYCoord(x: Float, y: Float) {
                                Log.d(TAG, "SET LAST XY COORD X: $x Y: $y")
                                CIPreferences.setChargingBubbleX(this@BatteryService, x)
                                CIPreferences.setChargingBubbleY(this@BatteryService, y)
                            }

                        })

                        if(mBubblesManager != null) {
                            val lastXCoord = CIPreferences.getChargingBubbleX(this).toInt()
                            val lastYCoord = CIPreferences.getChargingBubbleY(this).toInt()
                            Log.d(TAG, "LAST XY COORD X: $lastXCoord Y: $lastYCoord")
                            mBubblesManager?.addBubble(bubbleView, lastXCoord, lastYCoord)
                        }
                    }
                    .build()
            if(mBubblesManager != null) {
                mBubblesManager?.initialize()
            }
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
            if(mBubblesManager != null) {
                mBubblesManager?.recycle()
            }
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