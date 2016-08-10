package maderski.chargingindicator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Jason on 8/9/16.
 */
public class BatteryReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        PerformActions performActions = new PerformActions(context, new NotificationManager(context));
        performActions.showNotification(new Battery(intent));
    }
}
