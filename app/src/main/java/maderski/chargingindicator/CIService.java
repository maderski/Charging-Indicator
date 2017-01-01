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

    private static final String TAG = CIService.class.getName();

    private BatteryReceiver batteryReceiver;
    private BatteryManager batteryManager;
    private PowerConnectionReceiver pcr;

    //Instantiation of PowerConnectionReceiver and Registers receiver for ACTION_BATTERY_CHANGED
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        //Toast.makeText(getApplication(), "CIService Started", Toast.LENGTH_LONG).show();
        batteryManager = new BatteryManager(intent);
        batteryReceiver = new BatteryReceiver(batteryManager);
        batteryReceiver.onReceive(this, intent);
        this.registerReceiver(batteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        pcr = new PowerConnectionReceiver(batteryManager);
        pcr.onReceive(this, intent);

        if(BuildConfig.DEBUG)
            Log.i(TAG, "CIService Started");

        return Service.START_STICKY;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(BuildConfig.DEBUG)
            Log.i(TAG, "Service stopped");

        if(batteryReceiver != null) {
            if(BuildConfig.DEBUG)
                Log.i(TAG, "Battery Receiver NOT NULL!");
            this.unregisterReceiver(batteryReceiver);
        }
    }

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }
}
