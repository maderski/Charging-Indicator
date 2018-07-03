package maderski.chargingindicator.services

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log
import android.view.*
import com.txusballesteros.bubbles.BubbleLayout
import com.txusballesteros.bubbles.BubblesManager
import maderski.chargingindicator.R
import maderski.chargingindicator.interfaces.LastXYCoordListener
import maderski.chargingindicator.receivers.BatteryReceiver
import maderski.chargingindicator.sharedprefs.CIPreferences
import maderski.chargingindicator.ui.custom.CustomBubbleLayout
import maderski.chargingindicator.utils.ServiceUtils

class BatteryService : Service() {
    private val mBatteryReceiver = BatteryReceiver()

    private var mLayoutParams: WindowManager.LayoutParams? = null
    private var mIsShowingFAB = false
    private var mBubblesManager: BubblesManager? = null
    private var mWindowManager: WindowManager? = null
    private var mFloatingWidgetView: View? = null

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

//        if(mIsShowingFAB) {
//            mWindowManager = getSystemService(android.content.Context.WINDOW_SERVICE) as WindowManager
//            mFloatingWidgetView = LayoutInflater.from(this).inflate(R.layout.floating_widget_layout, null)
//
//            val windowType = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
//            else
//                WindowManager.LayoutParams.TYPE_PHONE
//
//            val windowFlags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//
//            mLayoutParams = WindowManager.LayoutParams(
//                    WindowManager.LayoutParams.WRAP_CONTENT,
//                    WindowManager.LayoutParams.WRAP_CONTENT,
//                    windowType,
//                    windowFlags,
//                    PixelFormat.TRANSPARENT
//            )
//
//            // Set ImageView initial position
//            mLayoutParams?.gravity = Gravity.CENTER
//
//            mFloatingWidgetView?.let {
//                if(mWindowManager != null) {
//                    mWindowManager?.addView(it, mLayoutParams)
//                }
//            }
//
//            setFloatingWidgetListeners()
//        }

        return START_NOT_STICKY
    }

//    private fun setFloatingWidgetListeners() {
//        mFloatingWidgetView?.let {
//            val chargingBoltIV: MovableFloatingImageView = it.findViewById(R.id.iv_charging_bolt)
//            chargingBoltIV.setActionMoveListener(object: ActionMoveListener {
//                override fun onActionMove(newX: Float, newY: Float) {
//                    it.animate()
//                            .x(newX)
//                            .y(newY)
//                            .setDuration(0)
//                            .start()
//                }
//
//            })
//        }
//    }

//    private fun setViewDimensions(view: View, heightWidthParams: Int) {
//        view.layoutParams = view.layoutParams.apply {
//            width = heightWidthParams
//            height = heightWidthParams
//        }
//
//        mWindowManager?.updateViewLayout(view, view.layoutParams)
//    }

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
//            mFloatingWidgetView?.let {
//                if(mWindowManager != null) {
//                    mWindowManager?.removeView(it)
//                }
//            }
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