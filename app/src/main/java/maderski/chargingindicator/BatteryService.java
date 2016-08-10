package maderski.chargingindicator;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Jason on 8/9/16.
 */
public class BatteryService extends Service {
    private BatteryReceiver batteryReceiver;

    public BatteryService(){ batteryReceiver = new BatteryReceiver(); }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        batteryReceiver.onReceive(this, intent);
        this.registerReceiver(batteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(batteryReceiver);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
