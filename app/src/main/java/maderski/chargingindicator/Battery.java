package maderski.chargingindicator;

import android.content.Intent;
import android.os.BatteryManager;

import java.text.NumberFormat;

/**
 * Created by Jason on 2/13/16.
 */
public class Battery {
    private Intent batteryStatus;
    private float previousPercent = 0;

    public Battery(Intent intent){
        batteryStatus = intent;
    }
    //Returns true or false depending if battery is charging
    public boolean isBatteryCharging(){
        return (batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1) == BatteryManager.BATTERY_STATUS_CHARGING);
    }
    //Returns battery percentage as string
    public String batteryLevel(){
        float percent = batteryPercent();
        NumberFormat percentFormat = NumberFormat.getPercentInstance();
        percentFormat.setMaximumFractionDigits(1);
        return percentFormat.format(percent);
    }

    private float batteryPercent(){
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        return level/(float)scale;
    }

    //Returns true or false depending if battery is plugged in
    public boolean isPluggedIn(){
        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
        boolean wirelessCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_WIRELESS;

        if(usbCharge || acCharge || wirelessCharge)
            return true;
        else
            return false;
    }

    public int isBatteryLevelIncreasing(){
        float currentPercent = batteryPercent();
        //Is decreasing
        int state = -1;

        //Is increasing
        if(currentPercent > previousPercent)
            state = 1;

        //Is the same
        else if(currentPercent == previousPercent)
            state = 0;

        previousPercent = currentPercent;

        return state;
    }
}
