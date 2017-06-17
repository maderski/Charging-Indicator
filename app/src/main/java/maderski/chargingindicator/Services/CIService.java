package maderski.chargingindicator.Services;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import maderski.chargingindicator.Actions.AsyncConnectedActions;
import maderski.chargingindicator.Battery.BatteryManager;
import maderski.chargingindicator.BuildConfig;
import maderski.chargingindicator.Receivers.PowerConnectionReceiver;
import maderski.chargingindicator.Utils.ServiceUtils;

/**
 * Created by Jason on 12/6/15.
 */
public class CIService extends Service {

    private static final String TAG = CIService.class.getName();

    private final PowerConnectionReceiver mPowerConnectionReceiver = new PowerConnectionReceiver();

    //Instantiation of PowerConnectionReceiver and Registers receiver for ACTION_BATTERY_CHANGED
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        Fabric.with(this, new Crashlytics());

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.ACTION_POWER_CONNECTED");
        filter.addAction("android.intent.action.ACTION_POWER_DISCONNECTED");

        registerReceiver(mPowerConnectionReceiver, filter);

        if(BuildConfig.DEBUG) {
            Log.i(TAG, "CIService Started");
            Toast.makeText(this, "CIService started", Toast.LENGTH_SHORT).show();
        }

        return Service.START_STICKY;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(BuildConfig.DEBUG)
            Log.i(TAG, "Service stopped");

        unregisterReceiver(mPowerConnectionReceiver);
    }

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }
}
