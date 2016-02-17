package maderski.chargingindicator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.text.NumberFormat;

/**
 * Created by Jason on 12/6/15.
 */
public class PowerConnectionReceiver extends BroadcastReceiver{

    private final static String TAG = PowerConnectionReceiver.class.getName();

    public static boolean powerConnected = false;
    public static boolean messageCreated = false;

    @Override
    public void onReceive(Context context, Intent intent){

        String action = "No Action";

        if(intent != null)
            if(intent.getAction() != null)
                action = intent.getAction();

        if(!CIService.isReceiverStarted) {
            Intent serviceIntent = new Intent(context, CIService.class);
            context.startService(serviceIntent);
        }

        switch (action){
            //When on BOOT_COMPLETED message is received run setNotifMessage
            case Intent.ACTION_BOOT_COMPLETED:
                //Toast.makeText(context,"BOOT COMPLETED", Toast.LENGTH_LONG).show();
                setNotifMessage(context, intent);
                break;
            //When POWER_CONNECTED is received create a toast message saying Power Connected
            case Intent.ACTION_POWER_CONNECTED:
                powerConnected = true;

                Toast.makeText(context, "Power Connected", Toast.LENGTH_LONG).show();
                Log.i(TAG, "Power Connected");
                break;
            //When POWER_DISCONNECTED is received create a toast message saying Power Disconnected
            case Intent.ACTION_POWER_DISCONNECTED:
                if(messageCreated) {
                    powerConnected = false;
                    messageCreated = false;

                    Notification.removeChargingMessage(context);
                    Toast.makeText(context, "Power Disconnected", Toast.LENGTH_LONG).show();
                    Log.i(TAG, "Power Disconnected");
                }
                break;
            //When BATTERY CHANGED is received run setNotifMessage
            case Intent.ACTION_BATTERY_CHANGED:
                Log.i(TAG, "ACTION BATTERY CHANGED");
                setNotifMessage(context, intent);
                break;
        }
    }
    //Create notification message with battery percentage
    private void setNotifMessage(Context context, Intent intent){
        String notifMessage;
        boolean isCharging = Battery.isBatteryCharging(intent);

        powerConnected = Battery.isPluggedIn(intent);

        Log.i(TAG, "setNotifMessage");
        if(powerConnected) {
            Log.i(TAG, "Power Connected: " + Boolean.toString(powerConnected));
            Log.i(TAG, Battery.batteryLevel(intent));
            if (isCharging) {
                Log.i(TAG, "isCharging: " + Boolean.toString(isCharging));
                //Toast.makeText(context, battery.batteryLevel(), Toast.LENGTH_LONG).show();
                notifMessage = "Battery Level: " + Battery.batteryLevel(intent);
            } else {
                //Toast.makeText(context, "Battery Charged!", Toast.LENGTH_LONG).show();
                Log.i(TAG, "Battery Charged!");
                notifMessage = "Battery Charged!";
            }

            if (!messageCreated) {
                Notification.createChargingMessage(context, notifMessage, getIcon(intent));
                messageCreated = true;
            } else
                Notification.updateChargingMessage(notifMessage, getIcon(intent));
        }
    }
    //Set type of icon depending on whether or not the phone is charging
    private int getIcon(Intent intent){
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
