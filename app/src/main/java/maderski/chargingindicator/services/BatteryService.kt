package maderski.chargingindicator.services

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.graphics.PixelFormat
import android.os.IBinder
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import maderski.chargingindicator.R
import maderski.chargingindicator.receivers.BatteryReceiver
import maderski.chargingindicator.sharedprefs.CIPreferences
import maderski.chargingindicator.utils.ServiceUtils

class BatteryService : Service() {
    private val mBatteryReceiver = BatteryReceiver()

    private var mIsShowingFAB = false
    private var mWindowManager: WindowManager? = null
    private var mOverLayView: View? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val filter = IntentFilter()
        registerReceiver(mBatteryReceiver, filter)

        mIsShowingFAB = CIPreferences.getShowChargingFab(this)

        if(mIsShowingFAB) {
            mWindowManager = getSystemService(android.content.Context.WINDOW_SERVICE) as WindowManager
            mOverLayView = LayoutInflater.from(this).inflate(R.layout.overlay_layout, null)

            val layoutParams = WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    PixelFormat.TRANSLUCENT
            )

            // Set FAB initial position
            layoutParams.gravity = Gravity.CENTER_VERTICAL or Gravity.START

            mOverLayView?.let {
                if(mWindowManager != null) {
                    mWindowManager?.addView(it, layoutParams)
                }
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

        setTheme(R.style.AppTheme)
    }

    override fun onDestroy() {
        super.onDestroy()
        if(mIsShowingFAB) {
            mOverLayView?.let {
                if(mWindowManager != null) {
                    mWindowManager?.removeView(it)
                }
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