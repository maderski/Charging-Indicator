package maderski.chargingindicator.actions;

import android.content.Context;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;

/**
 * Created by Jason on 2/11/17.
 */

public class AsyncDisconnectedActions extends AsyncTask<Void, Void, Void> {

    private PerformActions mPerformActions;
    private WeakReference<Context> mWeakReference;

    public AsyncDisconnectedActions(Context context){
        mWeakReference = new WeakReference<>(context.getApplicationContext());
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        final Context context = mWeakReference.get();
        mPerformActions = new PerformActions(context);
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
        final Context context = mWeakReference.get();
//        boolean isServiceRunning = ServiceUtils.isServiceRunning(context, BatteryService.class);
//        if(isServiceRunning) {
//            ServiceUtils.stopService(context, BatteryService.class, BatteryService.TAG);
//        }

//        CIPreferences.setPlayedChargingDoneSound(context, false);
    }
}