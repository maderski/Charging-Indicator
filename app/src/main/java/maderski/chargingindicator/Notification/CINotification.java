package maderski.chargingindicator.Notification;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import maderski.chargingindicator.R;


/**
 * Created by Jason on 12/6/15.
 */
public class CINotification {

    private final String nTAG = CINotification.class.getName();
    private final int nID = 607;

    private int color;

    private NotificationManager nManager;
    private NotificationCompat.Builder builder;

    public CINotification(Context context){
        nManager = (NotificationManager)context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(context);
        color = ContextCompat.getColor(context, R.color.colorPrimaryDark);
    }

    //Notification to display phone is charging
    public void createChargingMessage(String title, String message, int icon){
        builder.setContentTitle(message)
                .setContentText(title)
                .setSmallIcon(icon)
                .setOngoing(false)
                .setColor(color)
                .setAutoCancel(false);
        nManager.notify(nTAG, nID, builder.build());
    }

    //Remove notification message
    public void removeChargingMessage(Context context){
        try{
            nManager.cancel(nTAG, nID);
        }catch(Exception e){
            Log.e(nTAG, e.getMessage());
        }
    }

}
