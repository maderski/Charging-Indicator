package maderski.chargingindicator;

import android.content.Intent;
import android.os.BatteryManager;

import java.text.NumberFormat;

/**
 * Created by Jason on 2/13/16.
 */
public class Battery {
    //Returns true or false depending if battery is chargine
    public boolean isBatteryCharging(Intent batteryStatus){
        return (batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1) == BatteryManager.BATTERY_STATUS_CHARGING);
    }
    //Returns battery percentage as string
    public String batteryLevel(Intent batteryStatus){
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        float percent = level/(float)scale;

        NumberFormat percentFormat = NumberFormat.getPercentInstance();
        percentFormat.setMaximumFractionDigits(1);
        return percentFormat.format(percent);
    }
    //Returns true or false depending if battery is plugged in
    public boolean isPluggedIn(Intent batteryStatus){
        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
        boolean wirelessCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_WIRELESS;

        if(usbCharge || acCharge || wirelessCharge)
            return true;
        else
            return false;
    }
}
