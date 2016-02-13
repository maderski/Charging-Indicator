package maderski.chargingindicator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.text.NumberFormat;

/**
 * Created by Jason on 12/6/15.
 */
public class PowerConnectionReceiver extends BroadcastReceiver{

    private final static String TAG = PowerConnectionReceiver.class.getName();
    private static String message = "None";
    
    public static boolean powerConnected = false;
    @Override
    public void onReceive(Context context, Intent intent){
        Intent batteryIntent = intent;
        String action = "No Action";
        if(intent != null)
            if(intent.getAction() != null)
                action = intent.getAction();

        //If ACTION_POWER_CONNECTED broadcast is received create notification and display toast message
        if(action.equalsIgnoreCase(Intent.ACTION_POWER_CONNECTED)){
            powerConnected = true;

            if(Battery.isBatteryCharging(batteryIntent)){
                message = "Level: " + Battery.batteryLevel(batteryIntent);
            }else
                message = "Battery is Charged!";

            Notification.createChargingMessage(context, message);
            Toast.makeText(context, "Power Connected", Toast.LENGTH_LONG).show();

            Log.i(TAG, "Charging: " + Boolean.toString(Battery.isBatteryCharging(batteryIntent)));
            Log.i(TAG, "Level: " + Battery.batteryLevel(batteryIntent));
        }

        //If ACTION_POWER_DISCONNECTED is received remove notification and display toast message
        if(action.equalsIgnoreCase(Intent.ACTION_POWER_DISCONNECTED)){
            powerConnected = false;
            Notification.removeChargingMessage(context);
            Toast.makeText(context, "Power Disconnected", Toast.LENGTH_LONG).show();
        }

        if(action.equalsIgnoreCase(Intent.ACTION_BATTERY_CHANGED)){
            if(powerConnected) {
                if (Battery.isBatteryCharging(batteryIntent)) {
                    //Toast.makeText(context, battery.batteryLevel(), Toast.LENGTH_LONG).show();
                    Log.i(TAG, Battery.batteryLevel(batteryIntent));
                    message = "Battery Level: " + Battery.batteryLevel(batteryIntent);
                } else {
                    //Toast.makeText(context, "Battery Charged!", Toast.LENGTH_LONG).show();
                    Log.i(TAG, "Battery Charged!");
                    message = "Battery Charged!";
                }
                Notification.updateChargingMessage(message);
            }
        }
    }
    //Creates the Charging Indicator notification if the phone is already plugged in
    /*public void ifPluggedInAlready(Context context){
        powerConnected = true;

        if(battery.isBatteryCharging()){
            message = "Battery Level: " + battery.batteryLevel();
        }else
            message = "Battery is Charged!";

        Notification.createChargingMessage(context, message);
        Toast.makeText(context, "Power Connected", Toast.LENGTH_LONG).show();

        Log.i(TAG, "Charging: " + Boolean.toString(battery.isBatteryCharging()));
        Log.i(TAG, "Level: " + battery.batteryLevel());
    }*/
}
