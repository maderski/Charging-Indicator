package maderski.chargingindicator.Utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import java.util.List;

import maderski.chargingindicator.Services.BatteryService;
import maderski.chargingindicator.Services.CIService;

/**
 * Created by Jason on 6/17/17.
 */

public class ServiceUtils {
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

    public static void startCIService(Context context){
        if(!isServiceRunning(context, CIService.class)){
            Intent serviceIntent = new Intent(context, CIService.class);
            context.startService(serviceIntent);
        }
    }

    public static void restartBatteryService(Context context){
        Intent serviceIntent = new Intent(context, BatteryService.class);
        if (isServiceRunning(context, BatteryService.class)){
            context.stopService(serviceIntent);
        }

        context.startService(serviceIntent);
    }

    public static void stopBatteryService(Context context){
        Intent serviceIntent = new Intent(context, BatteryService.class);
        if (isServiceRunning(context, BatteryService.class)){
            context.stopService(serviceIntent);
        }
    }
}
