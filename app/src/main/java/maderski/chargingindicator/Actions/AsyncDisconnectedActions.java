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
    private Intent mIntent;
    private PerformActions mPerformActions;
    private BatteryManager mBatteryManager;

    public AsyncDisconnectedActions(Context context, Intent intent){
        this.mContext = context;
        this.mIntent = intent;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mBatteryManager = new BatteryManager(mIntent);
        mPerformActions = new PerformActions(mContext,
                new NotificationManager(mContext, mBatteryManager));
        mPerformActions.showToast("Power Disconnected");

    }

    @Override
    protected Void doInBackground(Void... params) {
        mPerformActions.disconnectVibrate();
        mPerformActions.disconnectSound();
        mPerformActions.removeNotification();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if(ServiceHelper.isServiceRunning(mContext, BatteryService.class)) {
            mContext.stopService(new Intent(mContext, BatteryService.class));
        }

        CIPreferences.setPlayedChargingDoneSound(mContext, false);
    }
}