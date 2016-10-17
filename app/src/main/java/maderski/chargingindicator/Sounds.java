package maderski.chargingindicator;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

/**
 * Created by Jason on 10/16/16.
 */

public class Sounds {
    private Context context;

    public Sounds(Context context){ this.context = context; }

    public void playDefaultNotificationSound(){
        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone ringtone = RingtoneManager.getRingtone(context, notificationSound);
        ringtone.play();
    }
}
