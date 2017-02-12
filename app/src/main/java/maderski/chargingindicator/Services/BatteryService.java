package maderski.chargingindicator.Services;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import maderski.chargingindicator.BatteryManager;
import maderski.chargingindicator.Receivers.BatteryReceiver;
import maderski.chargingindicator.BuildConfig;

/**
 * Created by Jason on 8/9/16.
 */
public class BatteryService extends Service {
    private static final String TAG = BatteryService.class.getName();

    private BatteryReceiver mBatteryReceiver;
    private BatteryManager mBatteryManager;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mBatteryManager = new BatteryManager(intent);
        mBatteryReceiver = new BatteryReceiver(mBatteryManager);
        mBatteryReceiver.onReceive(this, intent);
        this.registerReceiver(mBatteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mBatteryReceiver != null) {
            if(BuildConfig.DEBUG) {
                Log.i(TAG, "Battery Reciever NOT NULL!");
            }
            this.unregisterReceiver(mBatteryReceiver);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
