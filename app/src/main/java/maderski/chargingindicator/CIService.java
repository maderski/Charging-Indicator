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

    private PowerConnectionReceiver pcr;

    public CIService(){
        pcr = new PowerConnectionReceiver();
    }

    //Instantiation of PowerConnectionReceiver and Registers receiver for ACTION_BATTERY_CHANGED
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        //Toast.makeText(getApplication(), "CIService Started", Toast.LENGTH_LONG).show();
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
    }

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }
}
