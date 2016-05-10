package maderski.chargingindicator;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Jason on 5/1/16.
 */
public class NotificationManager {

    private final static String TAG = PowerConnectionReceiver.class.getName();

    public static boolean messageCreated = false;

    //Create notification message with battery percentage
    public static void setNotifMessage(Context context, Intent intent){
        boolean isCharging = Battery.isBatteryCharging(intent);

        boolean powerConnected = Battery.isPluggedIn(intent);

        Log.i(TAG, "setNotifMessage");
        if(powerConnected) {
            Log.i(TAG, "Power Connected: " + Boolean.toString(powerConnected));
            Log.i(TAG, Battery.batteryLevel(intent));

            Log.i(TAG, "isCharging: " + Boolean.toString(isCharging));
            //Toast.makeText(context, battery.batteryLevel(), Toast.LENGTH_LONG).show();
            String notifMessage = "Battery Level: " + Battery.batteryLevel(intent);
            Notification notification = new Notification(context);
            if (!messageCreated) {
                notification.createChargingMessage(context, notifMessage, getIcon(intent));
                messageCreated = true;
            } else
                notification.updateChargingMessage(notifMessage, getIcon(intent));
        }
    }
    //Set type of icon depending on whether or not the phone is charging
    private static int getIcon(Intent intent){
        final int CHARGING_ICON = R.mipmap.ic_launcher;
        final int FULL_BATTERY_ICON = android.R.drawable.ic_lock_idle_charging;

        boolean isCharging = Battery.isBatteryCharging(intent);
        String batteryLevel = Battery.batteryLevel(intent);


        if(!isCharging && batteryLevel.equalsIgnoreCase("100%")){
            return FULL_BATTERY_ICON;
        }else
            return CHARGING_ICON;
    }

    public static void removeNotifMessage(Context context){
        Notification notification = new Notification(context);
        notification.removeChargingMessage(context);
        messageCreated = false;
    }
}
