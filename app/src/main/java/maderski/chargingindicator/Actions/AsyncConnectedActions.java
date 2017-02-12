package maderski.chargingindicator.Actions;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import maderski.chargingindicator.Battery.BatteryManager;
import maderski.chargingindicator.CIPreferences;
import maderski.chargingindicator.Notification.NotificationManager;
import maderski.chargingindicator.Services.BatteryService;

/**
 * Created by Jason on 2/11/17.
 */

public class AsyncConnectedActions extends AsyncTask<Void, Void, Void> {

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
        CIPreferences.setPlayedChargingDoneSound(mContext, true);
        batteryManager = new BatteryManager(mIntent);
        performActions = new PerformActions(mContext,
                new NotificationManager(mContext, batteryManager));
        performActions.showToast("Power Connected");
    }

    @Override
    protected Void doInBackground(Void... params) {
        performActions.connectVibrate();
        performActions.connectSound();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        mContext.startService(new Intent(mContext, BatteryService.class));
    }
}
