package maderski.chargingindicator.actions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import maderski.chargingindicator.actions.interfaces.PerformActions
import maderski.chargingindicator.helpers.battery.BatteryHelper
import maderski.chargingindicator.helpers.bubbles.BubblesHelper
import maderski.chargingindicator.helpers.permission.PermissionHelper
import maderski.chargingindicator.helpers.sound.SoundHelper
import maderski.chargingindicator.helpers.vibration.VibrationHelper
import maderski.chargingindicator.sharedprefs.CIPreferences
import java.util.*

/**
 * Created by Jason on 8/2/16.
 */
class CIPerformActions(private val context: Context,
                       private val vibrationHelper: VibrationHelper,
                       private val playSoundHelper: SoundHelper,
                       private val bubblesHelper: BubblesHelper,
                       private val batteryHelper: BatteryHelper,
                       private val permissionHelper: PermissionHelper) : PerformActions {

    private val chargedSoundEnabled get() = CIPreferences.getBatteryChargedPlaySound(context)
    private val isBubbleShown: Boolean get() = CIPreferences.getShowChargingBubble(context)
    private val isQuietTime: Boolean
        get() {
            val quietTimeEnabled = CIPreferences.getQuietTime(context)
            val startQuietTime = CIPreferences.getStartQuietTime(context)
            val endQuietTime = CIPreferences.getEndQuietTime(context)

            return if (quietTimeEnabled) {
                val calendar = Calendar.getInstance()
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)
                val currentTime = hour * 100 + minute
                Log.d(TAG, "Current time: " + Integer.toString(currentTime) + " Start: " + Integer.toString(startQuietTime) + " End: " + Integer.toString(endQuietTime))

                currentTime in startQuietTime..endQuietTime
            } else {
                false
            }
        }

    private var toast: Toast? = null

    override fun connectVibrate() {
        val canVibrate = CIPreferences.getVibrateWhenPluggedIn(context)
        if (canVibrate && isQuietTime.not()) {
            if (CIPreferences.getDiffVibrations(context))
                vibrationHelper.onConnectPattern()
            else
                vibrationHelper.standardVibration()
        }
    }

    override fun disconnectVibrate() {
        val canVibrate = CIPreferences.getVibrateOnDisconnect(context)
        if (canVibrate && isQuietTime.not()) {
            if (CIPreferences.getDiffVibrations(context))
                vibrationHelper.onDisconnectPattern()
            else
                vibrationHelper.standardVibration()
        }
    }

    override fun connectSound() {
        val canPlaySound = CIPreferences.getPlaySound(context)
        val chosenPlaySound = CIPreferences.getChosenConnectSound(context)
        playSoundHandler(canPlaySound, chosenPlaySound)
    }

    override fun disconnectSound() {
        val canPlaySound = CIPreferences.getDisconnectPlaySound(context)
        val chosenPlaySound = CIPreferences.getChosenDisconnectSound(context)
        playSoundHandler(canPlaySound, chosenPlaySound)
    }

    override fun batteryChargedSound() {
        val canPlaySound = CIPreferences.getBatteryChargedPlaySound(context)
        val chosenPlaySound = CIPreferences.getChosenBatteryChargedSound(context)
        playSoundHandler(canPlaySound, chosenPlaySound)
    }

    override fun playSoundHandler(canPlaySound: Boolean, chosenPlaySound: String) {
        if (canPlaySound && isQuietTime.not()) {
            if (chosenPlaySound.equals("None", ignoreCase = true))
                playSoundHelper.playDefaultNotificationSound()
            else
                playSoundHelper.playNotificationSound(Uri.parse(chosenPlaySound))
        }
    }

    // Checks if charged sound is enabled and if battery is at 100%, if both are true only play the charged sound
    override fun playConnectSound(batteryStatus: Intent?) {
        if (chargedSoundEnabled && batteryStatus != null) {
            val isBatteryAt100 = batteryHelper.isBatteryAt100(batteryStatus)
            if (isBatteryAt100.not()) {
                connectSound()
            }
        } else {
            connectSound()
        }
    }

    override fun showToast(message: String) {
        val isToastShown = CIPreferences.getShowToast(context)
        if (isToastShown) {
            toast?.let {
                if (it.view.isShown) {
                    it.cancel()
                }
            }
            toast = Toast.makeText(context, message, Toast.LENGTH_LONG)
            toast?.show()
        }
    }

    override fun showBubble() {
        if (isBubbleShown) {
            val hasOverlayPermission = permissionHelper.hasOverlayPermission(context)
            if (hasOverlayPermission) {
                bubblesHelper.addBubble()
            } else {
                CIPreferences.setShowChargingBubble(context, false)
            }
        }
    }

    override fun removeBubble() {
        if (isBubbleShown)
            bubblesHelper.removeBubble()
    }

    companion object {
        private const val TAG = "PerformActions"
    }
}
