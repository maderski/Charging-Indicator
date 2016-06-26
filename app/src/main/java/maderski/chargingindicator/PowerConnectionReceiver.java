package maderski.chargingindicator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

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
            //When POWER_CONNECTED is received create a toast message saying Power Connected
            case Intent.ACTION_POWER_CONNECTED:
                NotificationManager.DoVibrateAndSound(context);
                if(CIPreferences.GetShowToast(context))
                    Toast.makeText(context, "Power Connected", Toast.LENGTH_LONG).show();

                if(BuildConfig.DEBUG)
                    Log.i(TAG, "Power Connected");
                break;
            //When POWER_DISCONNECTED is received create a toast message saying Power Disconnected
            case Intent.ACTION_POWER_DISCONNECTED:
                NotificationManager.RemoveNotifMessage(context);
                if(CIPreferences.GetShowToast(context))
                    Toast.makeText(context, "Power Disconnected", Toast.LENGTH_LONG).show();

                if(BuildConfig.DEBUG)
                    Log.i(TAG, "Power Disconnected");
                break;
            //When BATTERY CHANGED is received run SetNotifMessage
            case Intent.ACTION_BATTERY_CHANGED:
                if(BuildConfig.DEBUG)
                    Log.i(TAG, "ACTION BATTERY CHANGED");

                NotificationManager.SetNotifMessage(context, intent);
                break;
        }
    }
}
