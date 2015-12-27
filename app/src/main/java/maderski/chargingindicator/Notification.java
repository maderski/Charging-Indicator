package maderski.chargingindicator;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


/**
 * Created by Jason on 12/6/15.
 */
public class Notification {

    private static final String title = "Charging Indicator";
    private static final String message = "Phone is CHARGING";
    private static final String nTAG = Notification.class.getName();
    private static final int nID = 607;

    //Notification to display phone is charging
    public static void chargingMessage(Context context){
        NotificationManager nManager = (NotificationManager)context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(true)
                .setAutoCancel(false);
        nManager.notify(nTAG, nID, builder.build());
    }

    //Remove notification message
    public static void removeChargingMessage(Context context){
        NotificationManager nManager = (NotificationManager)context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        try{
            nManager.cancel(nTAG, nID);
        }catch(Exception e){
            Log.e(nTAG, e.getMessage());
        }
    }

}
