package maderski.chargingindicator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Jason on 8/9/16.
 */
public class BatteryReceiver extends BroadcastReceiver {
    private final static String TAG = BatteryReceiver.class.getName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Battery battery = new Battery(intent);

        if(battery.isPluggedIn()) {
            PerformActions performActions = new PerformActions(context, new NotificationManager(context, battery));
            performActions.showNotification();
            makeBatteryChargedSound(performActions, battery);
        }
    }

    private void makeBatteryChargedSound(PerformActions performActions, Battery battery){
        String batteryLvl = battery.batteryLevel();
        boolean isCharging = battery.isBatteryCharging();

        if(isCharging && batteryLvl.equalsIgnoreCase("100%")){
            performActions.batteryChargedSound();
        }
    }
}
