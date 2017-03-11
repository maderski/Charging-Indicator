package maderski.chargingindicator.Helpers;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import java.util.List;

import maderski.chargingindicator.Services.BatteryService;

/**
 * Created by Jason on 3/10/17.
 */

public class ServiceHelper {
    public static boolean isServiceRunning(Context context, Class<?> serviceClass){
        ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);

        for (ActivityManager.RunningServiceInfo runningServiceInfo : services) {
            if (runningServiceInfo.service.getClassName().equals(serviceClass.getName())){
                return true;
            }
        }
        return false;
    }

    public static void restartBatteryService(Context context){
        Intent serviceIntent = new Intent(context, BatteryService.class);
        if (ServiceHelper.isServiceRunning(context, BatteryService.class)){
            context.stopService(serviceIntent);
        }

        context.startService(serviceIntent);
    }
}
