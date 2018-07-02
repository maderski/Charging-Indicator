package maderski.chargingindicator.actions;

import android.content.Context;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;

import maderski.chargingindicator.services.BatteryService;
import maderski.chargingindicator.utils.ServiceUtils;

/**
 * Created by Jason on 2/11/17.
 */

public class AsyncConnectedActions extends AsyncTask<Void, Void, Void> {

    private PerformActions mPerformActions;
    private WeakReference<Context> mWeakReference;

    public AsyncConnectedActions(Context context) {
        mWeakReference = new WeakReference<>(context.getApplicationContext());
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        final Context context = mWeakReference.get();
        mPerformActions = new PerformActions(context);
        mPerformActions.showToast("Power Connected");
    }

    @Override
    protected Void doInBackground(Void... voids) {
        mPerformActions.connectVibrate();
        mPerformActions.connectSound();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        final Context context = mWeakReference.get();
        ServiceUtils.startService(context, BatteryService.class, BatteryService.TAG);
    }
}
