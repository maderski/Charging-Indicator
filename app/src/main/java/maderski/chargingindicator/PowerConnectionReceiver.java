package maderski.chargingindicator;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Jason on 12/6/15.
 */
public class PowerConnectionReceiver extends BroadcastReceiver{

    private final static String TAG = PowerConnectionReceiver.class.getName();

    @Override
    public void onReceive(Context context, Intent intent){

        if(intent != null)
            if(intent.getAction() != null) {
                String action = intent.getAction();
                PerformActions performActions = new PerformActions(context,
                        new NotificationManager(context, new Battery(intent)));
                actionReceived(context, action, performActions);
            }
    }

    private void actionReceived(Context context, String action, PerformActions performActions){
        if(BuildConfig.DEBUG) {
            Log.i(TAG, "Action: " + action);
        }

        switch (action){
            //When POWER_CONNECTED is received create a toast message saying Power Connected
            case Intent.ACTION_POWER_CONNECTED:
                new AsyncConnectedActions(performActions).execute();
                context.startService(new Intent(context, BatteryService.class));
                break;
            //When POWER_DISCONNECTED is received create a toast message saying Power Disconnected
            case Intent.ACTION_POWER_DISCONNECTED:
                new AsyncDisconnectedActions(performActions).execute();
                context.stopService(new Intent(context, BatteryService.class));
                break;
        }

    }

    private class AsyncConnectedActions extends AsyncTask<Void, Void, Void>{

        private PerformActions performActions;

        public AsyncConnectedActions(PerformActions performActions){
            this.performActions = performActions;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            performActions.showToast("Power Connected");
        }

        @Override
        protected Void doInBackground(Void... params) {
            performActions.vibrate();
            performActions.makeSound();
            return null;
        }
    }

    private class AsyncDisconnectedActions extends AsyncTask<Void, Void, Void>{

        private PerformActions performActions;

        public AsyncDisconnectedActions(PerformActions performActions){
            this.performActions = performActions;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            performActions.showToast("Power Disconnected");

        }

        @Override
        protected Void doInBackground(Void... params) {
            performActions.removeNotification();
            return null;
        }
    }
}
