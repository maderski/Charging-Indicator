package maderski.chargingindicator;

import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.util.Log;

/**
 * Created by Jason on 5/1/16.
 */
public class NotificationManager {

    private final static String TAG = PowerConnectionReceiver.class.getName();

    //Create notification message with battery percentage
    public static void SetNotifMessage(Context context, Intent intent){
        boolean isCharging = Battery.isBatteryCharging(intent);
        boolean powerConnected = Battery.isPluggedIn(intent);

        Log.i(TAG, "SetNotifMessage");
        if(powerConnected) {
            Log.i(TAG, "Power Connected: " + Boolean.toString(powerConnected));
            Log.i(TAG, Battery.batteryLevel(intent));

            Log.i(TAG, "isCharging: " + Boolean.toString(isCharging));
            //Toast.makeText(context, battery.batteryLevel(), Toast.LENGTH_LONG).show();
            Notification notification = new Notification(context);
            notification.createChargingMessage(getMessage(intent), getIcon(intent, context));

        }
    }

    public static void DoVibrateAndSound(Context context){
        boolean canVibrate = CIPreferences.GetVibrateWhenPluggedIn(context);
        boolean canPlaySound = CIPreferences.GetPlaySound(context);

        if(canPlaySound)
            playSound(context);

        if(canVibrate)
            vibrate(context);
    }

    //Set type of icon depending on whether or not the phone is charging
    private static int getIcon(Intent intent, Context context){
        final int CHARGING_ICON = R.mipmap.ic_launcher;
        final int FULL_BATTERY_ICON = android.R.drawable.ic_lock_idle_charging;

        boolean isCharging = Battery.isBatteryCharging(intent);
        boolean canChangeIcon = CIPreferences.GetChangeIcon(context);
        String batteryLevel = Battery.batteryLevel(intent);

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
    private static String getMessage(Intent intent){
        String batteryLvl = Battery.batteryLevel(intent);
        boolean isCharging = Battery.isBatteryCharging(intent);

        if(!isCharging && batteryLvl.equalsIgnoreCase("100%")){
            return "Battery is charged!";
        }else{
            return "Battery Level: " + batteryLvl;
        }
    }

    public static void RemoveNotifMessage(Context context){
        Notification notification = new Notification(context);
        notification.removeChargingMessage(context);
    }

    private static void vibrate(Context context){
        Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(500);
    }

    private static void playSound(Context context){
        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone ringtone = RingtoneManager.getRingtone(context, notificationSound);
        ringtone.play();

    }
}
