package maderski.chargingindicator;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Jason on 12/6/15.
 */
public class CIService extends Service {

    private String TAG = CIService.class.getName();

    public static boolean isReceiverStarted = false;

    //Called by the system every time a client explicitly starts the service by calling startService(Intent)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        //Toast.makeText(getApplication(), "CIService Started", Toast.LENGTH_LONG).show();
        Log.i(TAG, "CIService Started");
        isReceiverStarted = true;

        SharedObjects.pcr = new PowerConnectionReceiver();
        SharedObjects.pcr.onReceive(this, intent);
        this.registerReceiver(SharedObjects.pcr, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        return Service.START_STICKY;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.i(TAG, "Service DESTROYED");
        this.unregisterReceiver(SharedObjects.pcr);
        isReceiverStarted = false;
    }

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }
}
