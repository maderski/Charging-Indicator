package maderski.chargingindicator.Battery;

import android.content.Intent;
import android.util.Log;

import maderski.chargingindicator.BuildConfig;

/**
 * Created by Jason on 12/31/16.
 */

public class BatteryManager extends Battery {

    private static final String TAG = BatteryManager.class.getName();

    private float[] previousPercents = new float[7];
    private int index = 0;

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
        float previousPercent = getPreviousPercentAvg(currentPercent);
        System.out.println("AVERAGE: " + previousPercent);
        System.out.println("INDEX: " + index);

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

        return state;
    }

    private float getPreviousPercentAvg(float currentPercent) {
        if(index > previousPercents.length - 1) {
            index = 0;
        }

        previousPercents[index] = currentPercent;
//        if(index >= 1) {
//            if (currentPercent < previousPercents[index - 1]) {
//                return previousPercents[index - 1];
//            }
//        }

        float sum = 0;
        int numOfDivisibleElements = previousPercents.length;
        for(int i = 0; i < previousPercents.length; i++) {
            sum += previousPercents[i];
            System.out.println("Sum: " + sum + " Elements: " + previousPercents[i] + " divide by: " + numOfDivisibleElements);
        }
        index++;

        float avg = (sum / numOfDivisibleElements);

        System.out.println("AVG: " + avg);
        return avg;
    }
}
