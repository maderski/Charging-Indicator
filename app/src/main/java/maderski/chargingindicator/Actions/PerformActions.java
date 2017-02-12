package maderski.chargingindicator.Actions;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import maderski.chargingindicator.Actions.Actions;
import maderski.chargingindicator.BatteryManager;
import maderski.chargingindicator.CIPreferences;
import maderski.chargingindicator.NotificationManager;
import maderski.chargingindicator.Sounds;
import maderski.chargingindicator.Vibration;

/**
 * Created by Jason on 8/2/16.
 */
public class PerformActions implements Actions {
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

    @Override
    public void connectVibrate() {
        if(CIPreferences.GetVibrateWhenPluggedIn(context))
        {
            if (CIPreferences.getDiffVibrations(context))
                vibration.onConnectPattern();
            else
                vibration.standardVibration();
        }
    }

    @Override
    public void disconnectVibrate() {
        if(CIPreferences.getVibrateOnDisconnect(context)) {
            if (CIPreferences.getDiffVibrations(context))
                vibration.onDisconnectPattern();
            else
                vibration.standardVibration();
        }
    }

    @Override
    public void connectSound() {
        boolean canPlaySound = CIPreferences.GetPlaySound(context);
        String chosenPlaySound = CIPreferences.getChosenConnectSound(context);

        playSoundHandler(canPlaySound, chosenPlaySound);
    }

    @Override
    public void disconnectSound() {
        boolean canPlaySound = CIPreferences.getDisconnectPlaySound(context);
        String chosenPlaySound = CIPreferences.getChosenDisconnectSound(context);

        playSoundHandler(canPlaySound, chosenPlaySound);
    }

    @Override
    public void batteryChargedSound(){
        boolean canPlaySound = CIPreferences.getBatteryChargedPlaySound(context);
        String chosenPlaySound = CIPreferences.getChosenBatteryChargedSound(context);

        playSoundHandler(canPlaySound, chosenPlaySound);
    }

    private void playSoundHandler(boolean canPlaySound, String chosenPlaySound){
        if(canPlaySound){
            if(chosenPlaySound.equalsIgnoreCase("None"))
                playSound.playDefaultNotificationSound();
            else
                playSound.playNotificationSound(Uri.parse(chosenPlaySound));
        }
    }

    @Override
    public void showToast(String message) {
        if (CIPreferences.GetShowToast(context))
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showNotification() { notificationManager.SetNotifMessage(); }

    @Override
    public void removeNotification() { notificationManager.RemoveNotifMessage(); }

    public boolean makeBatteryChargedSound(Context context, PerformActions performActions, BatteryManager batteryManager, boolean canPlaySound){
        if(batteryManager.isBatteryAt100()
                && canPlaySound
                && CIPreferences.getBatteryChargedPlaySound(context)){
            performActions.batteryChargedSound();
            return false;
        }
        return canPlaySound;
    }
}