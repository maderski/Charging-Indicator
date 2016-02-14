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

    public static String message = "None";
    public static boolean powerConnected = false;

    @Override
    public void onReceive(Context context, Intent intent){

        String action = "No Action";
        if(intent != null)
            if(intent.getAction() != null)
                action = intent.getAction();

        //If ACTION_POWER_CONNECTED broadcast is received create notification and display toast message
        if(action.equalsIgnoreCase(Intent.ACTION_POWER_CONNECTED)){
            powerConnected = true;

            //message = "Checking...";

            //Notification.createChargingMessage(context, message);
            Toast.makeText(context, "Power Connected", Toast.LENGTH_LONG).show();

            Log.i(TAG, "Power Connected");
        }

        //If ACTION_POWER_DISCONNECTED is received remove notification and display toast message
        if(action.equalsIgnoreCase(Intent.ACTION_POWER_DISCONNECTED)){
            powerConnected = false;
            Notification.removeChargingMessage(context);
            Toast.makeText(context, "Power Disconnected", Toast.LENGTH_LONG).show();
            Log.i(TAG, "Power Disconnected");
        }

        if(action.equalsIgnoreCase(Intent.ACTION_BATTERY_CHANGED)){
            Log.i(TAG, "ACTION BATTERY CHANGED");
            powerConnected = Battery.isPluggedIn(intent);
            if(powerConnected) {
                Notification.createChargingMessage(context, PowerConnectionReceiver.message);
                if (Battery.isBatteryCharging(intent)) {
                    //Toast.makeText(context, battery.batteryLevel(), Toast.LENGTH_LONG).show();
                    Log.i(TAG, Battery.batteryLevel(intent));
                    message = "Battery Level: " + Battery.batteryLevel(intent);
                } else {
                    //Toast.makeText(context, "Battery Charged!", Toast.LENGTH_LONG).show();
                    Log.i(TAG, "Battery Charged!");
                    message = "Battery Charged!";
                }
                Notification.updateChargingMessage(message);
            }
        }
    }
}
