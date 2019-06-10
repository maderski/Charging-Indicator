package maderski.chargingindicator.serviceconnection

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import maderski.chargingindicator.services.CIService

class CIServiceConnection(val onConnectedTask: () -> Unit) : ServiceConnection {
    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        if (service is CIService.CIBinder) {
            val cIService = service.getService()
            onConnectedTask()
        }
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        // no-op
    }

}