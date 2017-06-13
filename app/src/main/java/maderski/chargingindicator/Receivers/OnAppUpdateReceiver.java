package maderski.chargingindicator.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import maderski.chargingindicator.CIPreferences;
import maderski.chargingindicator.Helpers.ServiceHelper;

/**
 * Created by Jason on 6/13/17.
 */

public class OnAppUpdateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //context.startService(new Intent(context, CIService.class));
        final Context ctx = context;
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(CIPreferences.getPlayedChargingDoneSound(ctx)){
                    CIPreferences.setPlayedChargingDoneSound(ctx, false);
                }
                ServiceHelper.restartBatteryService(ctx);
            }
        };
        handler.postDelayed(runnable, 5000);
    }
}
