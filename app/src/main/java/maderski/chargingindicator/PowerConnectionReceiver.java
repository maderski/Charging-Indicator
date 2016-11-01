package maderski.chargingindicator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

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
                Battery battery = new Battery(intent);
                PerformActions performActions = new PerformActions(context,
                        new NotificationManager(context, battery));
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
                new AsyncConnectedActions(context, performActions).execute();
                break;
            //When POWER_DISCONNECTED is received create a toast message saying Power Disconnected
            case Intent.ACTION_POWER_DISCONNECTED:
                new AsyncDisconnectedActions(context, performActions).execute();
                break;
        }

    }

    private class AsyncConnectedActions extends AsyncTask<Void, Void, Void>{

        private PerformActions performActions;
        private Context context;

        public AsyncConnectedActions(Context context, PerformActions performActions){
            this.performActions = performActions;
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            performActions.showToast("Power Connected");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            context.startService(new Intent(context, BatteryService.class));
        }

        @Override
        protected Void doInBackground(Void... params) {
            performActions.connectVibrate();
            performActions.connectSound();

            return null;
        }
    }

    private class AsyncDisconnectedActions extends AsyncTask<Void, Void, Void>{

        private PerformActions performActions;
        private Context context;

        public AsyncDisconnectedActions(Context context, PerformActions performActions){
            this.performActions = performActions;
            this.context = context;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            performActions.showToast("Power Disconnected");

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            context.stopService(new Intent(context, BatteryService.class));
        }

        @Override
        protected Void doInBackground(Void... params) {
            performActions.disconnectVibrate();
            performActions.disconnectSound();
            performActions.removeNotification();
            return null;
        }
    }
}
