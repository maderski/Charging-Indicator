package maderski.chargingindicator;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Jason on 8/9/16.
 */
public class BatteryService extends Service {
    private static final String TAG = BatteryService.class.getName();

    private BatteryReceiver batteryReceiver;

    private BatteryManager batteryManager;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        batteryManager = new BatteryManager(intent);
        batteryReceiver = new BatteryReceiver(batteryManager);
        batteryReceiver.onReceive(this, intent);
        this.registerReceiver(batteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(batteryReceiver != null) {
            if(BuildConfig.DEBUG) {
                Log.i(TAG, "Battery Reciever NOT NULL!");
            }
            this.unregisterReceiver(batteryReceiver);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

