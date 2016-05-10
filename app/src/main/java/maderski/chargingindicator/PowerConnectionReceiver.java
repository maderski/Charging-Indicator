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
                NotificationManager.setNotifMessage(context, intent);
                break;
            //When POWER_CONNECTED is received create a toast message saying Power Connected
            case Intent.ACTION_POWER_CONNECTED:
                Toast.makeText(context, "Power Connected", Toast.LENGTH_LONG).show();
                Log.i(TAG, "Power Connected");
                break;
            //When POWER_DISCONNECTED is received create a toast message saying Power Disconnected
            case Intent.ACTION_POWER_DISCONNECTED:
                if(NotificationManager.messageCreated) {
                    NotificationManager.removeNotifMessage(context);
                    Toast.makeText(context, "Power Disconnected", Toast.LENGTH_LONG).show();
                    Log.i(TAG, "Power Disconnected");
                }
                break;
            //When BATTERY CHANGED is received run setNotifMessage
            case Intent.ACTION_BATTERY_CHANGED:
                Log.i(TAG, "ACTION BATTERY CHANGED");
                NotificationManager.setNotifMessage(context, intent);
                break;
        }
    }
}
