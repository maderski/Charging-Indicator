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
    private Battery battery;

    public NotificationManager(Context context, Battery battery){
        super(context);
        this.context = context;
        this.battery = battery;
    }

    //Create notification message with battery percentage
    public void SetNotifMessage(){
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

            createChargingMessage(getMessage(), getIcon());
        }
    }

    //Set type of icon depending on whether or not the phone is charging
    private int getIcon(){
        final int CHARGING_ICON = chargingStateIcon();
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

    private int chargingStateIcon(){
        int stateIcon = R.drawable.standardbolt;

        if(CIPreferences.GetShowChargingStateIcon(context)) {
            int chargingState = battery.isBatteryLevelIncreasing();
            switch (chargingState) {
                case -1:
                    stateIcon = R.drawable.decreasingbolt;
                    break;
                case 0:
                    break;
                case 1:
                    stateIcon = R.drawable.increasingbolt;
                    break;
            }
        }

        return stateIcon;
    }

    //Set message depending on whether or not the phone is charging
    private String getMessage(){
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
            Battery.resetPreviousPercent();
        }
    }


}
