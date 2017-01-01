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

    private PerformActions performActions;
    private BatteryManager batteryManager;

    public PowerConnectionReceiver() {}
    public PowerConnectionReceiver(BatteryManager batteryManager) {
        this.batteryManager = batteryManager;
    }

    @Override
    public void onReceive(Context context, Intent intent){

        if(intent != null)
            if(intent.getAction() != null) {
                String action = intent.getAction();
                batteryManagerNullCheck(intent);
                this.performActions = new PerformActions(context,
                        new NotificationManager(context, batteryManager));
                actionReceived(context, action);
            }
    }

    private void batteryManagerNullCheck(Intent intent){
        if(batteryManager == null) {
            batteryManager = new BatteryManager(intent);
        }else {
            batteryManager.setBatteryStatus(intent);
        }
    }

    private void actionReceived(Context context, String action){
        if(BuildConfig.DEBUG) {
            Log.i(TAG, "Action: " + action);
        }

        switch (action){
            //When POWER_CONNECTED is received create a toast message saying Power Connected
            case Intent.ACTION_POWER_CONNECTED:
                Context[] contexts = { context };
                new AsyncConnectedActions().execute(contexts);
                break;
            //When POWER_DISCONNECTED is received create a toast message saying Power Disconnected
            case Intent.ACTION_POWER_DISCONNECTED:
                new AsyncDisconnectedActions().execute();
                break;
        }

    }

    private class AsyncConnectedActions extends AsyncTask<Context, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            performActions.showToast("Power Connected");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Context... params) {
            performActions.connectVibrate();
            if(CIPreferences.getBatteryChargedPlaySound(params[0])){
                if(!batteryManager.isBatteryAt100())
                    performActions.connectSound();
            }else{
                performActions.connectSound();
            }

            return null;
        }
    }

    private class AsyncDisconnectedActions extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            performActions.showToast("Power Disconnected");

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
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
