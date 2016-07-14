package maderski.chargingindicator;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;

/**
 * Created by Jason on 7/13/16.
 */
public class VibrateAndSound {
    public static void start(Context context){
        boolean canVibrate = CIPreferences.GetVibrateWhenPluggedIn(context);
        boolean canPlaySound = CIPreferences.GetPlaySound(context);

        if(canPlaySound)
            playSound(context);

        if(canVibrate)
            vibrate(context);
    }

    //Vibrate phone
    private static void vibrate(Context context){
        Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(500);
    }

    //Play default notification Sound
    private static void playSound(Context context){
        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone ringtone = RingtoneManager.getRingtone(context, notificationSound);
        ringtone.play();

    }
}
