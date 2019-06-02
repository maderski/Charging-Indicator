package maderski.chargingindicator.helpers.sound

import android.app.Activity
import android.net.Uri
import java.util.ArrayList

interface SoundHelper {
    val notificationSounds: ArrayList<String>

    fun playDefaultNotificationSound()
    fun playNotificationSound(ringtoneUri: Uri)
    fun notificationList(activity: Activity, chosenRingtone: String)
    fun notificationList(activity: Activity, chosenRingtone: String, resultCode: Int)
}