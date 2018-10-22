package maderski.chargingindicator.helpers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.widget.Toast

import java.util.ArrayList

/**
 * Created by Jason on 10/16/16.
 */

class SoundHelper(private val context: Context) {

    val notificationSounds: ArrayList<String>
        get() {
            val manager = RingtoneManager(context)
            manager.setType(RingtoneManager.TYPE_NOTIFICATION)
            val cursor = manager.cursor

            val notificationSoundList = ArrayList<String>()
            while (cursor.moveToNext()) {
                val id = cursor.getString(RingtoneManager.ID_COLUMN_INDEX)
                val uri = cursor.getString(RingtoneManager.URI_COLUMN_INDEX)
                notificationSoundList.add("$uri/$id")
            }
            return notificationSoundList
        }

    fun playDefaultNotificationSound() {
        val notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val ringtone = RingtoneManager.getRingtone(context, notificationSound)
        if (ringtone == null) {
            Toast.makeText(context, "No Default Notification sound set", Toast.LENGTH_LONG).show()
        } else {
            ringtone.play()
        }
    }

    fun playNotificationSound(ringtoneUri: Uri) {
        val ringtone = RingtoneManager.getRingtone(context, ringtoneUri)
        if (ringtone == null) {
            Toast.makeText(context, "Unable to play sound", Toast.LENGTH_LONG).show()
        } else {
            ringtone.play()
        }
    }

    fun notificationList(activity: Activity, chosenRingtone: String) {
        val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone")
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, if (chosenRingtone == "None") null else Uri.parse(chosenRingtone))
        activity.startActivityForResult(intent, 7)
    }

    fun notificationList(activity: Activity, chosenRingtone: String, resultCode: Int) {
        val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone")
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, if (chosenRingtone == "None") null else Uri.parse(chosenRingtone))
        activity.startActivityForResult(intent, resultCode)
    }
}
