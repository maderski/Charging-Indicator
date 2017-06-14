package maderski.chargingindicator.Actions;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.widget.Toast;

import maderski.chargingindicator.Battery.BatteryManager;
import maderski.chargingindicator.CIPreferences;
import maderski.chargingindicator.Notification.NotificationManager;
import maderski.chargingindicator.Receivers.BatteryReceiver;
import maderski.chargingindicator.Receivers.PowerConnectionReceiver;
import maderski.chargingindicator.Services.BatteryService;

/**
 * Created by Jason on 2/11/17.
 */

public class AsyncConnectedActions extends AsyncTask<Boolean, Void, Void> {

    private Context mContext;
    private PerformActions mPerformActions;

    public AsyncConnectedActions(Context context) {
        this.mContext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mPerformActions = new PerformActions(mContext);
        mPerformActions.showToast("Power Connected");
    }

    @Override
    protected Void doInBackground(Boolean... isAt100) {
        mPerformActions.connectVibrate();
        if(isAt100[0]) {
            mPerformActions.connectSound();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        mContext.startService(new Intent(mContext, BatteryService.class));
    }
}
