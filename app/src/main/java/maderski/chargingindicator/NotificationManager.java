package maderski.chargingindicator;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Jason on 5/1/16.
 */
public class NotificationManager extends CINotification{

    private final static String TAG = PowerConnectionReceiver.class.getName();

    private Context context;

    public NotificationManager(Context context){
        super(context);
        this.context = context;
    }

    //Create notification message with battery percentage
    public void SetNotifMessage(Battery battery){
        boolean isCharging = battery.isBatteryCharging();
        boolean powerConnected = battery.isPluggedIn();
        boolean showNotification = CIPreferences.GetShowNotification(context);

        if(BuildConfig.DEBUG)
            Log.i(TAG, "SetNotifMessage");

        if(powerConnected && showNotification) {
            if(BuildConfig.DEBUG) {
                Log.i(TAG, "Power Connected: " + Boolean.toString(powerConnected));
                Log.i(TAG, battery.batteryLevel());
                Log.i(TAG, "isCharging: " + Boolean.toString(isCharging));
            }

            createChargingMessage(getMessage(battery), getIcon(battery));
        }
    }

    //Set type of icon depending on whether or not the phone is charging
    private int getIcon(Battery battery){
        final int CHARGING_ICON = chargingStateIcon(battery);
        final int FULL_BATTERY_ICON = android.R.drawable.ic_lock_idle_charging;

        boolean isCharging = battery.isBatteryCharging();
        boolean canChangeIcon = CIPreferences.GetChangeIcon(context);
        String batteryLevel = battery.batteryLevel();

        if(canChangeIcon) {
            if (!isCharging && batteryLevel.equalsIgnoreCase("100%")) {
                return FULL_BATTERY_ICON;
            } else
                return CHARGING_ICON;
        }else{
            return CHARGING_ICON;
        }
    }

    private int chargingStateIcon(Battery battery){
        int chargingState = battery.isBatteryLevelIncreasing();
        int stateIcon = R.drawable.standardbolt;

        switch(chargingState){
            case -1:
                stateIcon = R.drawable.decreasingbolt;
                break;
            case 0:
                break;
            case 1:
                stateIcon = R.drawable.increasingbolt;
                break;
        }

        return stateIcon;
    }

    //Set message depending on whether or not the phone is charging
    private String getMessage(Battery battery){
        String batteryLvl = battery.batteryLevel();
        boolean isCharging = battery.isBatteryCharging();

        if(!isCharging && batteryLvl.equalsIgnoreCase("100%")){
            return "Battery is charged!";
        }else{
            return "Battery Level: " + batteryLvl;
        }
    }

    public void RemoveNotifMessage(){
        boolean showNotifcation = CIPreferences.GetShowNotification(context);

        if(showNotifcation) {
            removeChargingMessage(context);
        }
    }


}
