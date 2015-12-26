package maderski.chargingindicator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Jason on 12/6/15.
 */
public class PowerConnectionReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent){

        String action = "No Action";
        if(intent != null)
            if(intent.getAction() != null)
                action = intent.getAction();

        //If ACTION_POWER_CONNECTED broadcast is received create notification and display toast message
        if(action.equalsIgnoreCase(Intent.ACTION_POWER_CONNECTED)){
            Notification.chargingMessage(context);
            Toast.makeText(context, "Charging", Toast.LENGTH_LONG).show();
        }

        //If ACTION_POWER_DISCONNECTED is received remove notification and display toast message
        if(action.equalsIgnoreCase(Intent.ACTION_POWER_DISCONNECTED)){
            Notification.removeChargingMessage(context);
            Toast.makeText(context, "Not Charging", Toast.LENGTH_LONG).show();
        }

    }
}
