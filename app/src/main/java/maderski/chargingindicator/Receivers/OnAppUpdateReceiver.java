package maderski.chargingindicator.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import maderski.chargingindicator.Battery.BatteryManager;
import maderski.chargingindicator.CIPreferences;
import maderski.chargingindicator.Services.BatteryService;
import maderski.chargingindicator.Utils.ServiceUtils;

/**
 * Created by Jason on 6/13/17.
 */

public class OnAppUpdateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, final Intent intent) {
        //context.startService(new Intent(context, CIService.class));
        final Context ctx = context;
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(CIPreferences.getPlayedChargingDoneSound(ctx)){
                    CIPreferences.setPlayedChargingDoneSound(ctx, false);
                }
                ServiceUtils.restartBatteryService(context);
            }
        };
        handler.postDelayed(runnable, 5000);

        ServiceUtils.startCIService(context);
    }
}
