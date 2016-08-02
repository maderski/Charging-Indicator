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
    private PerformActions performActions;

    @Override
    public void onReceive(Context context, Intent intent){

        String action = "No Action";

        if(intent != null)
            if(intent.getAction() != null) {
                action = intent.getAction();
                performActions = new PerformActions(context, new Battery(intent));
            }

        if(!isServiceRunning(context, CIService.class)) {
            Intent serviceIntent = new Intent(context, CIService.class);
            context.startService(serviceIntent);
        }

        if(BuildConfig.DEBUG) {
            Log.i(TAG, "Action: " + action);
        }

        switch (action){
            //When POWER_CONNECTED is received create a toast message saying Power Connected
            case Intent.ACTION_POWER_CONNECTED:
                performActions.vibrate();
                performActions.makeSound();
                performActions.showToast("Power Connected");
                break;
            //When POWER_DISCONNECTED is received create a toast message saying Power Disconnected
            case Intent.ACTION_POWER_DISCONNECTED:
                performActions.removeNotification();
                performActions.showToast("Power Disconnected");
                break;
            //When BATTERY CHANGED is received run SetNotifMessage
            case Intent.ACTION_BATTERY_CHANGED:
                performActions.showNotification();
                break;
        }
    }

    private boolean isServiceRunning(Context context, Class<?> serviceClass){
        ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);

        for (ActivityManager.RunningServiceInfo runningServiceInfo : services) {
            if (runningServiceInfo.service.getClassName().equals(serviceClass.getName())){
                return true;
            }
        }
        return false;
    }
}
