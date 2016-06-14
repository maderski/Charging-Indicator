package maderski.chargingindicator;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Jason on 12/6/15.
 */
public class CIService extends Service {

    private String TAG = CIService.class.getName();

    public static boolean isReceiverStarted = false;
    public static boolean messageCreated = false;

    private PowerConnectionReceiver pcr;

    //Instantiation of PowerConnectionReceiver and Registers receiver for ACTION_BATTERY_CHANGED
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        //Toast.makeText(getApplication(), "CIService Started", Toast.LENGTH_LONG).show();
        Log.i(TAG, "CIService Started");
        isReceiverStarted = true;

        pcr = new PowerConnectionReceiver();
        pcr.onReceive(this, intent);
        this.registerReceiver(pcr, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        return Service.START_STICKY;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.i(TAG, "Service DESTROYED");
        this.unregisterReceiver(pcr);
        isReceiverStarted = false;
    }

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }
}
