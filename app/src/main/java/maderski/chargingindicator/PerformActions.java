package maderski.chargingindicator;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

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
        Battery.resetPreviousPercent();
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
        if(CIPreferences.GetPlaySound(context)){
            if(CIPreferences.getChosenConnectSound(context).equalsIgnoreCase("None"))
                playSound.playDefaultNotificationSound();
            else
                playSound.playNotificationSound(Uri.parse(CIPreferences.getChosenConnectSound(context)));
        }
    }

    @Override
    public void disconnectSound() {
        if(CIPreferences.getDisconnectPlaySound(context)){
            if(CIPreferences.getChosenDisconnectSound(context).equalsIgnoreCase("None"))
                playSound.playDefaultNotificationSound();
            else
                playSound.playNotificationSound(Uri.parse(CIPreferences.getChosenDisconnectSound(context)));
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
}
