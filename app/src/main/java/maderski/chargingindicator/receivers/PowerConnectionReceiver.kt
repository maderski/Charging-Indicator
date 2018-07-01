package maderski.chargingindicator.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import maderski.chargingindicator.actions.AsyncConnectedActions
import maderski.chargingindicator.actions.AsyncDisconnectedActions

class PowerConnectionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            val action = it.action
            if(action != null) {
                when(action) {
                    Intent.ACTION_POWER_CONNECTED -> AsyncConnectedActions(context).execute()
                    Intent.ACTION_POWER_DISCONNECTED -> AsyncDisconnectedActions(context).execute()
                }
            }
        }
    }

}