package maderski.chargingindicator.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import maderski.chargingindicator.BatteryManager;
import maderski.chargingindicator.CIPreferences;
import maderski.chargingindicator.NotificationManager;
import maderski.chargingindicator.Actions.PerformActions;

/**
 * Created by Jason on 8/9/16.
 */
public class BatteryReceiver extends BroadcastReceiver {
    private final static String TAG = BatteryReceiver.class.getName();

    private boolean canPlaySound = true;
    private BatteryManager batteryManager;

    public BatteryReceiver(){}

    public BatteryReceiver(BatteryManager batteryManager) { this.batteryManager = batteryManager; }

    @Override
    public void onReceive(Context context, Intent intent) {
        batteryManager.setBatteryStatus(intent);
        if(batteryManager.isPluggedIn()) {
            PerformActions performActions = new PerformActions(context, new NotificationManager(context, batteryManager));
            performActions.showNotification();
            makeBatteryChargedSound(context, performActions, batteryManager);
        }
    }

    private void makeBatteryChargedSound(Context context, PerformActions performActions, BatteryManager batteryManager){
        if(batteryManager.isBatteryAt100()
                && canPlaySound
                && CIPreferences.getBatteryChargedPlaySound(context)){
            performActions.batteryChargedSound();
            canPlaySound = false;
        }
    }
}
