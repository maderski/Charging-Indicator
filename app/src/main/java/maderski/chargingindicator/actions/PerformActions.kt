package maderski.chargingindicator.actions

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast

import java.util.Calendar

import maderski.chargingindicator.CIPreferences
import maderski.chargingindicator.Sounds
import maderski.chargingindicator.Vibration

/**
 * Created by Jason on 8/2/16.
 */
class PerformActions(private val mContext: Context) {
    private val vibration: Vibration = Vibration(mContext)
    private val playSound: Sounds = Sounds(mContext)

    private val isQuietTime: Boolean
        get() {
            val quietTimeEnabled = CIPreferences.getQuietTime(mContext)
            val startQuietTime = CIPreferences.getStartQuietTime(mContext)
            val endQuietTime = CIPreferences.getEndQuietTime(mContext)

            if (quietTimeEnabled) {
                val c = Calendar.getInstance()
                val hour = c.get(Calendar.HOUR_OF_DAY)
                val minute = c.get(Calendar.MINUTE)
                val currentTime = hour * 100 + minute
                Log.d(TAG, "Current time: " + Integer.toString(currentTime) + " Start: " + Integer.toString(startQuietTime) + " End: " + Integer.toString(endQuietTime))

                val isQuiet = if (currentTime >= 1200) {
                    currentTime >= startQuietTime
                } else {
                    currentTime <= endQuietTime
                }
                Log.d(TAG, "Current time: " + Integer.toString(currentTime) + " Start: " +
                        Integer.toString(startQuietTime) + " End: " +
                        Integer.toString(endQuietTime) + " QuietTime: " + java.lang.Boolean.toString(isQuiet))

                return isQuiet
            } else {
                return false
            }
        }

    fun connectVibrate() {
        val canVibrate = CIPreferences.GetVibrateWhenPluggedIn(mContext)
        if (canVibrate && isQuietTime.not()) {
            if (CIPreferences.getDiffVibrations(mContext))
                vibration.onConnectPattern()
            else
                vibration.standardVibration()
        }
    }

    fun disconnectVibrate() {
        val canVibrate = CIPreferences.getVibrateOnDisconnect(mContext)
        if (canVibrate && isQuietTime.not()) {
            if (CIPreferences.getDiffVibrations(mContext))
                vibration.onDisconnectPattern()
            else
                vibration.standardVibration()
        }
    }

    fun connectSound() {
        val canPlaySound = CIPreferences.GetPlaySound(mContext)
        val chosenPlaySound = CIPreferences.getChosenConnectSound(mContext)

        playSoundHandler(canPlaySound, chosenPlaySound)
    }

    fun disconnectSound() {
        val canPlaySound = CIPreferences.getDisconnectPlaySound(mContext)
        val chosenPlaySound = CIPreferences.getChosenDisconnectSound(mContext)

        playSoundHandler(canPlaySound, chosenPlaySound)
    }

    fun batteryChargedSound() {
        val canPlaySound = CIPreferences.getBatteryChargedPlaySound(mContext)
        val chosenPlaySound = CIPreferences.getChosenBatteryChargedSound(mContext)

        playSoundHandler(canPlaySound, chosenPlaySound)
    }

    private fun playSoundHandler(canPlaySound: Boolean, chosenPlaySound: String) {
        if (canPlaySound && isQuietTime.not()) {
            if (chosenPlaySound.equals("None", ignoreCase = true))
                playSound.playDefaultNotificationSound()
            else
                playSound.playNotificationSound(Uri.parse(chosenPlaySound))
        }
    }

    fun showToast(message: String) {
        if (CIPreferences.GetShowToast(mContext))
            Toast.makeText(mContext, message, Toast.LENGTH_LONG).show()
    }

    companion object {
        private const val TAG = "PerformActions"
    }
}
