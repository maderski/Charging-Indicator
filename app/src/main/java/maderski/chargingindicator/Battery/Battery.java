package maderski.chargingindicator.Battery;

import android.content.Intent;
import android.os.BatteryManager;

import java.text.NumberFormat;

/**
 * Created by Jason on 2/13/16.
 */
public abstract class Battery {
    private static final String TAG = Battery.class.getName();

    private Intent batteryStatus;

    public void setBatteryStatus(Intent batteryStatus) {
        this.batteryStatus = batteryStatus;
    }

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

    public float batteryPercent(){
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
}
