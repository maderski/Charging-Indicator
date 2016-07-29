package maderski.chargingindicator;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Jason on 5/1/16.
 */
public class NotificationManager {

    private final static String TAG = PowerConnectionReceiver.class.getName();

    private Battery battery;

    public NotificationManager(Battery battery){
        this.battery = battery;
    }

    //Create notification message with battery percentage
    public void SetNotifMessage(Context context, Intent intent){
        boolean isCharging = battery.isBatteryCharging(intent);
        boolean powerConnected = battery.isPluggedIn(intent);
        boolean showNotification = CIPreferences.GetShowNotification(context);

        if(BuildConfig.DEBUG)
            Log.i(TAG, "SetNotifMessage");

        if(powerConnected && showNotification) {
            if(BuildConfig.DEBUG) {
                Log.i(TAG, "Power Connected: " + Boolean.toString(powerConnected));
                Log.i(TAG, battery.batteryLevel(intent));
                Log.i(TAG, "isCharging: " + Boolean.toString(isCharging));
            }
            Notification notification = new Notification(context);
            notification.createChargingMessage(getMessage(intent), getIcon(intent, context));
        }
    }

    //Set type of icon depending on whether or not the phone is charging
    private int getIcon(Intent intent, Context context){
        final int CHARGING_ICON = R.mipmap.ic_launcher;
        final int FULL_BATTERY_ICON = android.R.drawable.ic_lock_idle_charging;

        boolean isCharging = battery.isBatteryCharging(intent);
        boolean canChangeIcon = CIPreferences.GetChangeIcon(context);
        String batteryLevel = battery.batteryLevel(intent);

        if(canChangeIcon) {
            if (!isCharging && batteryLevel.equalsIgnoreCase("100%")) {
                return FULL_BATTERY_ICON;
            } else
                return CHARGING_ICON;
        }else{
            return CHARGING_ICON;
        }
    }
    //Set message depending on whether or not the phone is charging
    private String getMessage(Intent intent){
        String batteryLvl = battery.batteryLevel(intent);
        boolean isCharging = battery.isBatteryCharging(intent);

        if(!isCharging && batteryLvl.equalsIgnoreCase("100%")){
            return "Battery is charged!";
        }else{
            return "Battery Level: " + batteryLvl;
        }
    }

    public void RemoveNotifMessage(Context context){
        boolean showNotifcation = CIPreferences.GetShowNotification(context);

        if(showNotifcation) {
            Notification notification = new Notification(context);
            notification.removeChargingMessage(context);
        }
    }


}
