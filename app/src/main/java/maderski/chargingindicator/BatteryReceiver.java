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
        Battery battery = new Battery(intent);

        if(battery.isPluggedIn()) {
            PerformActions performActions = new PerformActions(context, new NotificationManager(context, battery));
            performActions.showNotification();
        }
    }
}
