package maderski.chargingindicator;

import android.content.Intent;
import android.util.Log;

/**
 * Created by Jason on 12/31/16.
 */

public class BatteryManager extends Battery {

    private static final String TAG = BatteryManager.class.getName();

    private float previousPercent;

    public BatteryManager(Intent intent){
        super(intent);
    }

    public boolean isBatteryAt100(){
        if(BuildConfig.DEBUG) {
            Log.i(TAG, "Battery Percent is: " + Float.toString(batteryPercent()));
            Log.i(TAG, "Battery is at 100? " + Boolean.toString(batteryPercent() == 1.0));
        }
        return batteryPercent() == 1.0;
    }

    public int getBatteryChargingState(){
        float currentPercent = batteryPercent();
        if(BuildConfig.DEBUG)
            Log.i(TAG, "Current %: " + currentPercent + " Previous %: " + previousPercent);

        //Is decreasing
        int state = -1;

        //Is increasing
        if(currentPercent > previousPercent){
            state = 1;
        }
        //Is the same
        else if(currentPercent == previousPercent){
            state = 0;
        }

        previousPercent = currentPercent;

        return state;
    }
}
