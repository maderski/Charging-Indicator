package maderski.chargingindicator;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.widget.Toast;

/**
 * Created by Jason on 8/2/16.
 */
public class PerformActions implements Actions {
    private Context context;
    private NotificationManager notificationManager;

    public PerformActions(Context context, NotificationManager notificationManager){
        this.context = context;
        this.notificationManager = notificationManager;
    }
    @Override
    public void vibrate() {
        boolean canVibrate = CIPreferences.GetVibrateWhenPluggedIn(context);
        if(canVibrate) {
            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(500);
        }
    }

    @Override
    public void makeSound() {
        boolean canPlaySound = CIPreferences.GetPlaySound(context);
        if(canPlaySound) {
            Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone ringtone = RingtoneManager.getRingtone(context, notificationSound);
            ringtone.play();
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
