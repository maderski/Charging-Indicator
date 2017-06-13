package maderski.chargingindicator.Notification;

import android.content.Context;
import android.util.Log;

import maderski.chargingindicator.Battery.BatteryManager;
import maderski.chargingindicator.BuildConfig;
import maderski.chargingindicator.CIPreferences;
import maderski.chargingindicator.R;
import maderski.chargingindicator.Receivers.PowerConnectionReceiver;

/**
 * Created by Jason on 5/1/16.
 */
public class NotificationManager extends CINotification {

    private final static String TAG = PowerConnectionReceiver.class.getName();

    private Context context;
    private BatteryManager batteryManager;

    public NotificationManager(Context context, BatteryManager batteryManager){
        super(context);
        this.context = context;
        this.batteryManager = batteryManager;
    }

    //Create notification message with battery percentage
    public void setNotifMessage(){
        boolean isCharging = batteryManager.isBatteryCharging();
        boolean powerConnected = batteryManager.isPluggedIn();
        boolean showNotification = CIPreferences.GetShowNotification(context);

        if(BuildConfig.DEBUG)
            Log.i(TAG, "setNotifMessage");

        if(powerConnected && showNotification) {
            if(BuildConfig.DEBUG) {
                Log.i(TAG, "Power Connected: " + Boolean.toString(powerConnected));
                Log.i(TAG, batteryManager.batteryLevel());
                Log.i(TAG, "isCharging: " + Boolean.toString(isCharging));
            }

            createChargingMessage(getMessage(), getIcon());
        }
    }

    //Set type of icon depending on whether or not the phone is charging
    private int getIcon(){
        final int CHARGING_ICON = chargingStateIcon();
        final int FULL_BATTERY_ICON = android.R.drawable.ic_lock_idle_charging;

        boolean canChangeIcon = CIPreferences.GetChangeIcon(context);

        if(canChangeIcon) {
            if (batteryManager.isBatteryAt100()) {
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
            int chargingState = batteryManager.getBatteryChargingState();
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

    private String getTitle(){

        return "Charging Indicator";
    }

    //Set message depending on whether or not the phone is charging
    private String getMessage(){
        String batteryLvl = batteryManager.batteryLevel();

        if(batteryManager.isBatteryAt100()){
            return "Battery is charged!";
        }else{
            return "Battery Level: " + batteryLvl;
        }
    }

    public void removeNotifMessage(){
        boolean showNotifcation = CIPreferences.GetShowNotification(context);

        if(showNotifcation) {
            removeChargingMessage(context);
        }
    }


}
