package maderski.chargingindicator;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Jason on 12/6/15.
 */
public class PowerConnectionReceiver extends BroadcastReceiver{

    private final static String TAG = PowerConnectionReceiver.class.getName();

    @Override
    public void onReceive(Context context, Intent intent){

        if(intent != null)
            if(intent.getAction() != null) {
                String action = intent.getAction();
                PerformActions performActions = new PerformActions(context,
                        new NotificationManager(context));
                actionReceived(context, action, performActions);
            }
    }

    private void actionReceived(Context context, String action, PerformActions performActions){
        if(BuildConfig.DEBUG) {
            Log.i(TAG, "Action: " + action);
        }

        switch (action){
            //When POWER_CONNECTED is received create a toast message saying Power Connected
            case Intent.ACTION_POWER_CONNECTED:
                performActions.vibrate();
                performActions.makeSound();
                performActions.showToast("Power Connected");
                context.startService(new Intent(context, BatteryService.class));
                break;
            //When POWER_DISCONNECTED is received create a toast message saying Power Disconnected
            case Intent.ACTION_POWER_DISCONNECTED:
                performActions.removeNotification();
                performActions.showToast("Power Disconnected");
                context.stopService(new Intent(context, BatteryService.class));
                break;
        }

    }
}
