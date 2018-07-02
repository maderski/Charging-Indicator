package maderski.chargingindicator.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class BatteryReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            val action = it.action
            if(action != null) {
                Log.d(TAG, action)
            }
        }
    }

    companion object {
        const val TAG = "BatteryReceiver"
    }
}