package maderski.chargingindicator.Actions;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.AsyncTask;

import maderski.chargingindicator.Battery.BatteryManager;
import maderski.chargingindicator.CIPreferences;
import maderski.chargingindicator.Helpers.ServiceHelper;
import maderski.chargingindicator.Notification.NotificationManager;
import maderski.chargingindicator.Receivers.BatteryReceiver;
import maderski.chargingindicator.Services.BatteryService;

/**
 * Created by Jason on 2/11/17.
 */

public class AsyncDisconnectedActions extends AsyncTask<Void, Void, Void> {

    private Context mContext;
    private PerformActions mPerformActions;

    public AsyncDisconnectedActions(Context context){
        this.mContext = context;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mPerformActions = new PerformActions(mContext);
        mPerformActions.showToast("Power Disconnected");
    }

    @Override
    protected Void doInBackground(Void... params) {
        mPerformActions.disconnectVibrate();
        mPerformActions.disconnectSound();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if(ServiceHelper.isServiceRunning(mContext, BatteryService.class)) {
            mContext.stopService(new Intent(mContext, BatteryService.class));
        }

        NotificationManager notificationManager = new NotificationManager(mContext, new BatteryManager(new Intent()));
        notificationManager.removeNotifMessage();

        CIPreferences.setPlayedChargingDoneSound(mContext, false);
    }
}