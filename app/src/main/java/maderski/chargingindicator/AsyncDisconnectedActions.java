package maderski.chargingindicator;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

/**
 * Created by Jason on 2/11/17.
 */

public class AsyncDisconnectedActions extends AsyncTask<Void, Void, Void> {

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
    protected Void doInBackground(Void... params) {
        performActions.disconnectVibrate();
        performActions.disconnectSound();
        performActions.removeNotification();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        mContext.stopService(new Intent(mContext, BatteryService.class));
    }
}