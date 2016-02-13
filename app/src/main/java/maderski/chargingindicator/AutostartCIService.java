package maderski.chargingindicator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Jason on 12/6/15.
 */
public class AutostartCIService extends BroadcastReceiver{

    private String TAG = AutostartCIService.class.getName();

    //Automatically starts the CIService on boot
    public void onReceive(Context context, Intent intent){
        intent = new Intent(context, CIService.class);
        context.startService(intent);
        Log.i(TAG, "CIService AUTO-STARTED");
    }
}
