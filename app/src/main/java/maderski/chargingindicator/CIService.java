package maderski.chargingindicator;

import android.app.Service;
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
    private PowerConnectionReceiver pcr;
    public static boolean isReceiverStarted = false;

    //Called by the system every time a client explicitly starts the service by calling startService(Intent)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        //Toast.makeText(getApplication(), "CIService Started", Toast.LENGTH_LONG).show();
        Log.i(TAG, "CIService Started");
        isReceiverStarted = true;
        pcr = new PowerConnectionReceiver();
        pcr.onReceive(this, intent);
        //IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        //Battery.batteryStatus = this.registerReceiver(pcr, iFilter);

        //Battery.batteryStatus = this.registerReceiver(null, iFilter);
        this.registerReceiver(pcr, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        unregisterReceiver(pcr);
        isReceiverStarted = false;
    }

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }
}
