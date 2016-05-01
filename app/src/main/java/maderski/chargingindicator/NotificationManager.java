package maderski.chargingindicator;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Jason on 5/1/16.
 */
public class NotificationManager {

    private final static String TAG = PowerConnectionReceiver.class.getName();

    public static boolean powerConnected = false;
    public static boolean messageCreated = false;

    //Create notification message with battery percentage
    public static void setNotifMessage(Context context, Intent intent){
        boolean isCharging = Battery.isBatteryCharging(intent);

        powerConnected = Battery.isPluggedIn(intent);

        Log.i(TAG, "setNotifMessage");
        if(powerConnected) {
            Log.i(TAG, "Power Connected: " + Boolean.toString(powerConnected));
            Log.i(TAG, Battery.batteryLevel(intent));

            Log.i(TAG, "isCharging: " + Boolean.toString(isCharging));
            //Toast.makeText(context, battery.batteryLevel(), Toast.LENGTH_LONG).show();
            String notifMessage = "Battery Level: " + Battery.batteryLevel(intent);

            if (!messageCreated) {
                Notification.createChargingMessage(context, notifMessage, getIcon(intent));
                messageCreated = true;
            } else
                Notification.updateChargingMessage(notifMessage, getIcon(intent));
        }
    }
    //Set type of icon depending on whether or not the phone is charging
    public static int getIcon(Intent intent){
        final int CHARGING_ICON = R.mipmap.ic_launcher;
        final int FULL_BATTERY_ICON = android.R.drawable.ic_lock_idle_charging;

        boolean isCharging = Battery.isBatteryCharging(intent);
        String batteryLevel = Battery.batteryLevel(intent);


        if(!isCharging){
            return FULL_BATTERY_ICON;
        }else if(batteryLevel.equalsIgnoreCase("100%")){
            return FULL_BATTERY_ICON;
        }else
            return CHARGING_ICON;
    }
}
