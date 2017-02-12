package maderski.chargingindicator.Actions;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

import maderski.chargingindicator.Battery.BatteryManager;
import maderski.chargingindicator.CIPreferences;
import maderski.chargingindicator.Notification.NotificationManager;
import maderski.chargingindicator.Sounds;
import maderski.chargingindicator.Vibration;

/**
 * Created by Jason on 8/2/16.
 */
public class PerformActions {
    private static final String TAG = "PerformActions";
    private Context context;
    private NotificationManager notificationManager;
    private Vibration vibration;
    private Sounds playSound;

    public PerformActions(Context context, NotificationManager notificationManager){
        this.context = context;
        this.notificationManager = notificationManager;
        this.vibration = new Vibration(context);
        this.playSound = new Sounds(context);
    }

    public void connectVibrate() {
        if(CIPreferences.GetVibrateWhenPluggedIn(context))
        {
            if (CIPreferences.getDiffVibrations(context))
                vibration.onConnectPattern();
            else
                vibration.standardVibration();
        }
    }

    public void disconnectVibrate() {
        if(CIPreferences.getVibrateOnDisconnect(context)) {
            if (CIPreferences.getDiffVibrations(context))
                vibration.onDisconnectPattern();
            else
                vibration.standardVibration();
        }
    }

    public void connectSound(BatteryManager batteryManager) {
        boolean canPlaySound = CIPreferences.GetPlaySound(context);
        String chosenPlaySound = CIPreferences.getChosenConnectSound(context);

        if(!batteryManager.isBatteryAt100())
            playSoundHandler(canPlaySound, chosenPlaySound);
    }

    public void disconnectSound() {
        boolean canPlaySound = CIPreferences.getDisconnectPlaySound(context);
        String chosenPlaySound = CIPreferences.getChosenDisconnectSound(context);

        playSoundHandler(canPlaySound, chosenPlaySound);
    }

    public void batteryChargedSound(){
        boolean canPlaySound = CIPreferences.getBatteryChargedPlaySound(context);
        String chosenPlaySound = CIPreferences.getChosenBatteryChargedSound(context);

        playSoundHandler(canPlaySound, chosenPlaySound);
    }

    private void playSoundHandler(boolean canPlaySound, String chosenPlaySound){
        if(canPlaySound && !isQuietTime()){
            if(chosenPlaySound.equalsIgnoreCase("None"))
                playSound.playDefaultNotificationSound();
            else
                playSound.playNotificationSound(Uri.parse(chosenPlaySound));
        }
    }

    private boolean isQuietTime() {
        boolean quietTimeEnabled = CIPreferences.getQuietTime(context);
        int startQuietTime = CIPreferences.getStartQuietTime(context);
        int endQuietTime = CIPreferences.getEndQuietTime(context);

        if(quietTimeEnabled) {
            Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            int currentTime = (hour * 100) + minute;
            Log.d(TAG, "Current time: " + Integer.toString(currentTime));
            if (currentTime >= startQuietTime){
                return true;
            } else if(currentTime <= endQuietTime && !(currentTime <= startQuietTime)) {
                return true;
            }else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void showToast(String message) {
        if (CIPreferences.GetShowToast(context))
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public void showNotification() { notificationManager.SetNotifMessage(); }

    public void removeNotification() { notificationManager.RemoveNotifMessage(); }

    public void makeBatteryChargedSound(Context context, PerformActions performActions, BatteryManager batteryManager){
        boolean canPlaySound = CIPreferences.getPlayedChargingDoneSound(context);
        if(batteryManager.isBatteryAt100()
                && canPlaySound
                && CIPreferences.getBatteryChargedPlaySound(context)){
            performActions.batteryChargedSound();
            CIPreferences.setPlayedChargingDoneSound(context, false);
        }
    }
}
