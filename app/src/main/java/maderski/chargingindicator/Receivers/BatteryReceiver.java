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
    private static final String TAG = "BatteryReceiver";

    private boolean canPlaySound = true;
    private BatteryManager mBatteryManager;

    public BatteryReceiver(){}

    public BatteryReceiver(BatteryManager batteryManager) { mBatteryManager = batteryManager;}

    @Override
    public void onReceive(Context context, Intent intent) {
        mBatteryManager.setBatteryStatus(intent);
        if(mBatteryManager.isPluggedIn()) {
            PerformActions performActions = new PerformActions(context, new NotificationManager(context, mBatteryManager));
            performActions.showNotification();
            canPlaySound = performActions.makeBatteryChargedSound(context, performActions, mBatteryManager, canPlaySound);
        }
    }
}