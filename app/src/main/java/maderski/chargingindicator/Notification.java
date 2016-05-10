package maderski.chargingindicator;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;


/**
 * Created by Jason on 12/6/15.
 */
public class Notification {

    private final String title = "Charging Indicator";
    private final String nTAG = Notification.class.getName();
    private final int nID = 607;

    private static NotificationManager nManager;
    private static NotificationCompat.Builder builder;

    public Notification(Context context){
        nManager = (NotificationManager)context
                .getSystemService(Context.NOTIFICATION_SERVICE);
    }

    //Notification to display phone is charging
    public void createChargingMessage(Context context, String message, int icon){
        int color = ContextCompat.getColor(context, R.color.colorPrimaryDark);

        builder = new NotificationCompat.Builder(context)
                .setContentTitle(message)
                .setContentText(title)
                .setSmallIcon(icon)
                .setOngoing(false)
                .setColor(color)
                .setAutoCancel(false);
        nManager.notify(nTAG, nID, builder.build());
    }
    //Updates Notification
    public void updateChargingMessage(String message, int icon){
        builder.setContentTitle(message)
                .setSmallIcon(icon);
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
