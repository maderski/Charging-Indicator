package maderski.chargingindicator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Jason on 12/6/15.
 */
public class AutostartCIService extends BroadcastReceiver{

    private String TAG = AutostartCIService.class.getName();

    //Automatically starts the CIService on boot
    public void onReceive(Context arg0, Intent arg1){
        Intent intent = new Intent(arg0, CIService.class);
        arg0.startService(intent);
        Log.i(TAG, "CIService AUTO-STARTED");
    }
}
