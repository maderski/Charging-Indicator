package maderski.chargingindicator.actions

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import maderski.chargingindicator.helpers.CIBubblesHelper
import maderski.chargingindicator.helpers.SoundHelper
import maderski.chargingindicator.helpers.VibrationHelper

import java.util.Calendar

import maderski.chargingindicator.sharedprefs.CIPreferences

/**
 * Created by Jason on 8/2/16.
 */
class PerformActions(private val mContext: Context) {
    private val mVibrationHelper: VibrationHelper = VibrationHelper(mContext)
    private val mPlaySoundHelper: SoundHelper = SoundHelper(mContext)
    private val mCIBubblesHelper: CIBubblesHelper = CIBubblesHelper(mContext)

    private val isBubbleShown: Boolean = CIPreferences.getShowChargingBubble(mContext)
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
        val canVibrate = CIPreferences.getVibrateWhenPluggedIn(mContext)
        if (canVibrate && isQuietTime.not()) {
            if (CIPreferences.getDiffVibrations(mContext))
                mVibrationHelper.onConnectPattern()
            else
                mVibrationHelper.standardVibration()
        }
    }

    fun disconnectVibrate() {
        val canVibrate = CIPreferences.getVibrateOnDisconnect(mContext)
        if (canVibrate && isQuietTime.not()) {
            if (CIPreferences.getDiffVibrations(mContext))
                mVibrationHelper.onDisconnectPattern()
            else
                mVibrationHelper.standardVibration()
        }
    }

    fun connectSound() {
        val canPlaySound = CIPreferences.getPlaySound(mContext)
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
                mPlaySoundHelper.playDefaultNotificationSound()
            else
                mPlaySoundHelper.playNotificationSound(Uri.parse(chosenPlaySound))
        }
    }

    fun showToast(message: String) {
        val isToastShown = CIPreferences.getShowToast(mContext)
        if (isToastShown)
            Toast.makeText(mContext, message, Toast.LENGTH_LONG).show()
    }

    fun showBubble() {
        if(isBubbleShown)
            mCIBubblesHelper.addBubble()
    }

    fun removeBubble() {
        if(isBubbleShown)
            mCIBubblesHelper.removeBubble()
    }

    companion object {
        private const val TAG = "PerformActions"
    }
}
