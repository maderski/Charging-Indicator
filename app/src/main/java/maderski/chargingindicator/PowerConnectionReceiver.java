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

    private class AsyncConnectedActions extends AsyncTask<Void, Void, Void>{

        private Context mContext;
        private Intent mIntent;
        private PerformActions performActions;
        private BatteryManager batteryManager;

        public AsyncConnectedActions(Context context, Intent intent) {
            this.mContext = context;
            this.mIntent = intent;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mContext.startService(new Intent(mContext, BatteryService.class));
            batteryManager = new BatteryManager(mIntent);
            performActions = new PerformActions(mContext,
                    new NotificationManager(mContext, batteryManager));
            performActions.showToast("Power Connected");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... params) {
            performActions.connectVibrate();
            performActions.connectSound();
            return null;
        }
    }

    private class AsyncDisconnectedActions extends AsyncTask<Void, Void, Void>{

        private Context mContext;
        private Intent mIntent;
        private PerformActions performActions;
        private BatteryManager batteryManager;

        public AsyncDisconnectedActions(Context context, Intent intent){
            this.mContext = context;
            this.mIntent = intent;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            batteryManager = new BatteryManager(mIntent);
            performActions = new PerformActions(mContext,
                    new NotificationManager(mContext, batteryManager));
            performActions.showToast("Power Disconnected");

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mContext.stopService(new Intent(mContext, BatteryService.class));
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
