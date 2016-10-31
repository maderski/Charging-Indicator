package maderski.chargingindicator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import java.util.ArrayList;

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

    public void playNotificationSound(Uri ringtoneUri){
        Ringtone ringtone = RingtoneManager.getRingtone(context, ringtoneUri);
        ringtone.play();
    }

    public ArrayList<String> getNotificationSounds(){
        RingtoneManager manager = new RingtoneManager(context);
        manager.setType(RingtoneManager.TYPE_NOTIFICATION);
        Cursor cursor = manager.getCursor();

        ArrayList<String> notificationSoundList = new ArrayList<>();
        while(cursor.moveToNext()){
            String id = cursor.getString(RingtoneManager.ID_COLUMN_INDEX);
            String uri = cursor.getString(RingtoneManager.URI_COLUMN_INDEX);
            notificationSoundList.add(uri + "/" + id);
        }
        return notificationSoundList;
    }

    public void notificationList(Activity activity, String chosenRingtone){
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone");
        //intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, (Uri) null);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, (chosenRingtone == "None") ? (Uri)null :Uri.parse(chosenRingtone));
        activity.startActivityForResult(intent, 7);
    }

    public void notificationList(Activity activity, String chosenRingtone, int resultCode){
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone");
        //intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, (Uri) null);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, (chosenRingtone == "None") ? (Uri)null :Uri.parse(chosenRingtone));
        activity.startActivityForResult(intent, resultCode);
    }
}
