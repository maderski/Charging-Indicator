package maderski.chargingindicator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.util.Log;

/**
 * Created by Jason on 12/6/15.
 */
public class PowerConnectionReceiver extends BroadcastReceiver{

    private final static String TAG = PowerConnectionReceiver.class.getName();

    public PowerConnectionReceiver() {}

    @Override
    public void onReceive(Context context, Intent intent){

        if(intent != null)
            if(intent.getAction() != null) {
                String action = intent.getAction();
                actionReceived(context, intent, action);
            }
    }

    private void actionReceived(Context context, Intent intent, String action){
        if(BuildConfig.DEBUG) {
            Log.i(TAG, "Action: " + action);
        }

        switch (action){
            //When POWER_CONNECTED is received create a toast message saying Power Connected
            case Intent.ACTION_POWER_CONNECTED:
                new AsyncConnectedActions(context, intent).execute();
                break;
            //When POWER_DISCONNECTED is received create a toast message saying Power Disconnected
            case Intent.ACTION_POWER_DISCONNECTED:
                new AsyncDisconnectedActions(context, intent).execute();
                break;
        }

    }
}
