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
    private NotificationManager notificationManager;

    @Override
    public void onReceive(Context context, Intent intent){

        String action = "No Action";

        if(intent != null)
            if(intent.getAction() != null) {
                action = intent.getAction();
                notificationManager = new NotificationManager(new Battery());
            }

        if(!CIService.isReceiverStarted) {
            Intent serviceIntent = new Intent(context, CIService.class);
            context.startService(serviceIntent);
        }

        if(BuildConfig.DEBUG) {
            Log.i(TAG, "Action: " + action);
        }

        switch (action){
            //When POWER_CONNECTED is received create a toast message saying Power Connected
            case Intent.ACTION_POWER_CONNECTED:
                VibrateAndSound vibrateAndSound = new VibrateAndSound();
                vibrateAndSound.start(context);
                if (CIPreferences.GetShowToast(context))
                    Toast.makeText(context, "Power Connected", Toast.LENGTH_LONG).show();
                break;
            //When POWER_DISCONNECTED is received create a toast message saying Power Disconnected
            case Intent.ACTION_POWER_DISCONNECTED:
                notificationManager.RemoveNotifMessage(context);
                if(CIPreferences.GetShowToast(context))
                    Toast.makeText(context, "Power Disconnected", Toast.LENGTH_LONG).show();
                break;
            //When BATTERY CHANGED is received run SetNotifMessage
            case Intent.ACTION_BATTERY_CHANGED:
                notificationManager.SetNotifMessage(context, intent);
                break;
        }
    }
}
