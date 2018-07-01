package maderski.chargingindicator.actions;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

import maderski.chargingindicator.CIPreferences;
import maderski.chargingindicator.Sounds;
import maderski.chargingindicator.Vibration;

/**
 * Created by Jason on 8/2/16.
 */
public class PerformActions {
    private static final String TAG = "PerformActions";
    private Context context;
    private Vibration vibration;
    private Sounds playSound;

    public PerformActions(Context context){
        this.context = context;
        this.vibration = new Vibration(context);
        this.playSound = new Sounds(context);
    }

    public void connectVibrate() {
        if(CIPreferences.GetVibrateWhenPluggedIn(context) && !isQuietTime())
        {
            if (CIPreferences.getDiffVibrations(context))
                vibration.onConnectPattern();
            else
                vibration.standardVibration();
        }
    }

    public void disconnectVibrate() {
        if(CIPreferences.getVibrateOnDisconnect(context) && !isQuietTime()) {
            if (CIPreferences.getDiffVibrations(context))
                vibration.onDisconnectPattern();
            else
                vibration.standardVibration();
        }
    }

    public void connectSound() {
        boolean canPlaySound = CIPreferences.GetPlaySound(context);
        String chosenPlaySound = CIPreferences.getChosenConnectSound(context);

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
            Log.d(TAG, "Current time: " + Integer.toString(currentTime) + " Start: " + Integer.toString(startQuietTime) + " End: " + Integer.toString(endQuietTime));
            boolean isQuiet = false;
            if(currentTime >= 1200){
                isQuiet = (currentTime >= startQuietTime);
            } else {
                isQuiet = (currentTime <= endQuietTime);
            }
            Log.d(TAG, "Current time: " + Integer.toString(currentTime) + " Start: " +
                    Integer.toString(startQuietTime) + " End: " +
                    Integer.toString(endQuietTime) + " QuietTime: " + Boolean.toString(isQuiet));

            return isQuiet;
        } else {
            return false;
        }
    }

    public void showToast(String message) {
        if (CIPreferences.GetShowToast(context))
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

//    public void makeBatteryChargedSound(Context context, PerformActions performActions, CIBatteryManager CIBatteryManager){
//        boolean playChargedSoundEnabled = CIPreferences.getBatteryChargedPlaySound(context);
//        boolean playedSound = CIPreferences.getPlayedChargingDoneSound(context);
//        if(CIBatteryManager.isBatteryAt100()
//                && playChargedSoundEnabled
//                && !playedSound) {
//            performActions.batteryChargedSound();
//            CIPreferences.setPlayedChargingDoneSound(context, true);
//        }
//    }
}
