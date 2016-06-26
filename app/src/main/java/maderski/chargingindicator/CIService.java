package maderski.chargingindicator;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Debug;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Jason on 12/6/15.
 */
public class CIService extends Service {

    private String TAG = CIService.class.getName();

    public static boolean isReceiverStarted = false;

    private static PowerConnectionReceiver pcr;
    private static Context receiverContext;

    //Instantiation of PowerConnectionReceiver and Registers receiver for ACTION_BATTERY_CHANGED
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        //Toast.makeText(getApplication(), "CIService Started", Toast.LENGTH_LONG).show();
        pcr = new PowerConnectionReceiver();
        pcr.onReceive(this, intent);
        this.registerReceiver(pcr, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        receiverContext = this;

        if(BuildConfig.DEBUG)
            Log.i(TAG, "CIService Started");

        isReceiverStarted = true;

        return Service.START_STICKY;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(BuildConfig.DEBUG)
            Log.i(TAG, "Service stopped");

        this.unregisterReceiver(pcr);
        isReceiverStarted = false;
    }

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    public static void RestartReceiver(){
        receiverContext.unregisterReceiver(pcr);
        receiverContext.registerReceiver(pcr, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        if(BuildConfig.DEBUG) {
            String tag = CIService.class.getName();
            Log.i(tag, "Service Restarted");
        }
    }
}
