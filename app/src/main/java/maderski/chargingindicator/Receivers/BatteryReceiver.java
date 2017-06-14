package maderski.chargingindicator.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import maderski.chargingindicator.Battery.BatteryManager;
import maderski.chargingindicator.Notification.NotificationManager;
import maderski.chargingindicator.Actions.PerformActions;
import maderski.chargingindicator.Services.BatteryService;

/**
 * Created by Jason on 8/9/16.
 */
public class BatteryReceiver extends BroadcastReceiver {
    private static final String TAG = "BatteryReceiver";

    private BatteryManager mBatteryManager;

    public BatteryReceiver(){}

    public BatteryReceiver(BatteryManager batteryManager) { mBatteryManager = batteryManager;}

    @Override
    public void onReceive(Context context, Intent intent) {
        mBatteryManager.setBatteryStatus(intent);
        if(mBatteryManager.isPluggedIn()) {
            PerformActions performActions = new PerformActions(context);
            performActions.makeBatteryChargedSound(context, performActions, mBatteryManager);

            NotificationManager notificationManager = new NotificationManager(context, mBatteryManager);
            notificationManager.setNotifMessage();
        }
    }
}
