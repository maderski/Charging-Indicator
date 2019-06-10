package maderski.chargingindicator.sharedprefs

import android.content.Context
import android.content.SharedPreferences
import maderski.chargingindicator.sharedprefs.CISettings.Companion.BATTERY_CHARGED_PERCENT
import maderski.chargingindicator.sharedprefs.CISettings.Companion.BATTERY_CHARGED_PLAY_SOUND_KEY
import maderski.chargingindicator.sharedprefs.CISettings.Companion.CHARGING_BUBBLE_X
import maderski.chargingindicator.sharedprefs.CISettings.Companion.CHARGING_BUBBLE_Y
import maderski.chargingindicator.sharedprefs.CISettings.Companion.CHOSEN_BATTERY_CHARGED_SOUND_KEY
import maderski.chargingindicator.sharedprefs.CISettings.Companion.CHOSEN_CONNECT_SOUND_KEY
import maderski.chargingindicator.sharedprefs.CISettings.Companion.CHOSEN_DISCONNECT_SOUND_KEY
import maderski.chargingindicator.sharedprefs.CISettings.Companion.DIFF_VIBRATIONS_KEY
import maderski.chargingindicator.sharedprefs.CISettings.Companion.DISCONNECT_PLAY_SOUND_KEY
import maderski.chargingindicator.sharedprefs.CISettings.Companion.DISCONNECT_VIBRATE_KEY
import maderski.chargingindicator.sharedprefs.CISettings.Companion.END_QUIET_TIME
import maderski.chargingindicator.sharedprefs.CISettings.Companion.PLAY_SOUND_KEY
import maderski.chargingindicator.sharedprefs.CISettings.Companion.QUIET_TIME
import maderski.chargingindicator.sharedprefs.CISettings.Companion.SHOW_CHARGING_BUBBLE_KEY
import maderski.chargingindicator.sharedprefs.CISettings.Companion.SHOW_TOAST_KEY
import maderski.chargingindicator.sharedprefs.CISettings.Companion.START_QUIET_TIME
import maderski.chargingindicator.sharedprefs.CISettings.Companion.VIBRATE_KEY

/**
 * Created by Jason on 6/11/16.
 */
object CIPreferences : CISettings {
    private const val MY_PREFS_NAME = "CIPreferences"
    private const val NONE = "None"

    override fun setBatteryChargedPercent(context: Context, percent: Int) = editor(context).putInt(BATTERY_CHARGED_PERCENT, percent).apply()
    override fun getBatteryCharged(context: Context): Int = reader(context).getInt(BATTERY_CHARGED_PERCENT, 100)

    override fun setChargingBubbleX(context: Context, x: Float) = editor(context).putFloat(CHARGING_BUBBLE_X, x).apply()
    override fun getChargingBubbleX(context: Context): Float = reader(context).getFloat(CHARGING_BUBBLE_X, 60f)

    override fun setChargingBubbleY(context: Context, y: Float) = editor(context).putFloat(CHARGING_BUBBLE_Y, y).apply()
    override fun getChargingBubbleY(context: Context): Float = reader(context).getFloat(CHARGING_BUBBLE_Y, 20f)

    override fun setShowChargingBubble(context: Context, showChargingFab: Boolean) = editor(context).putBoolean(SHOW_CHARGING_BUBBLE_KEY, showChargingFab).apply()
    override fun getShowChargingBubble(context: Context): Boolean = reader(context).getBoolean(SHOW_CHARGING_BUBBLE_KEY, false)

    override fun setStartQuietTime(context: Context, time: Int) = editor(context).putInt(START_QUIET_TIME, time).apply()
    override fun getStartQuietTime(context: Context): Int = reader(context).getInt(START_QUIET_TIME, 2200)

    override fun setEndQuietTime(context: Context, time: Int) = editor(context).putInt(END_QUIET_TIME, time).apply()
    override fun getEndQuietTime(context: Context): Int = reader(context).getInt(END_QUIET_TIME, 800)

    override fun setQuietTime(context: Context, enabled: Boolean) = editor(context).putBoolean(QUIET_TIME, enabled).apply()
    override fun getQuietTime(context: Context): Boolean = reader(context).getBoolean(QUIET_TIME, false)

    override fun setBatteryChargedPlaySound(context: Context, enabled: Boolean) = editor(context).putBoolean(BATTERY_CHARGED_PLAY_SOUND_KEY, enabled).apply()
    override fun getBatteryChargedPlaySound(context: Context): Boolean = reader(context).getBoolean(BATTERY_CHARGED_PLAY_SOUND_KEY, false)

    override fun setChosenBatteryChargedSound(context: Context, chosenSound: String) = editor(context).putString(CHOSEN_BATTERY_CHARGED_SOUND_KEY, chosenSound).apply()
    override fun getChosenBatteryChargedSound(context: Context): String = reader(context).getString(CHOSEN_BATTERY_CHARGED_SOUND_KEY, NONE) ?: NONE

    override fun setChosenDisconnectSound(context: Context, chosenSound: String) = editor(context).putString(CHOSEN_DISCONNECT_SOUND_KEY, chosenSound).apply()
    override fun getChosenDisconnectSound(context: Context): String = reader(context).getString(CHOSEN_DISCONNECT_SOUND_KEY, NONE) ?: NONE

    override fun setChosenConnectSound(context: Context, chosenSound: String) = editor(context).putString(CHOSEN_CONNECT_SOUND_KEY, chosenSound).apply()
    override fun getChosenConnectSound(context: Context): String = reader(context).getString(CHOSEN_CONNECT_SOUND_KEY, NONE) ?: NONE

    override fun setDisconnectPlaySound(context: Context, enabled: Boolean) = editor(context).putBoolean(DISCONNECT_PLAY_SOUND_KEY, enabled).apply()
    override fun getDisconnectPlaySound(context: Context): Boolean = reader(context).getBoolean(DISCONNECT_PLAY_SOUND_KEY, false)

    override fun setVibrateOnDisconnect(context: Context, enabled: Boolean) = editor(context).putBoolean(DISCONNECT_VIBRATE_KEY, enabled).apply()
    override fun getVibrateOnDisconnect(context: Context): Boolean = reader(context).getBoolean(DISCONNECT_VIBRATE_KEY, false)

    override fun setDiffVibrations(context: Context, enabled: Boolean) = editor(context).putBoolean(DIFF_VIBRATIONS_KEY, enabled).apply()
    override fun getDiffVibrations(context: Context): Boolean = reader(context).getBoolean(DIFF_VIBRATIONS_KEY, true)

    override fun setVibrateWhenPluggedIn(context: Context, enabled: Boolean) = editor(context).putBoolean(VIBRATE_KEY, enabled).apply()
    override fun getVibrateWhenPluggedIn(context: Context): Boolean = reader(context).getBoolean(VIBRATE_KEY, false)

    override fun setPlaySound(context: Context, enabled: Boolean) = editor(context).putBoolean(PLAY_SOUND_KEY, enabled).apply()
    override fun getPlaySound(context: Context): Boolean = reader(context).getBoolean(PLAY_SOUND_KEY, false)

    override fun setShowToast(context: Context, enabled: Boolean) = editor(context).putBoolean(SHOW_TOAST_KEY, enabled).apply()
    override fun getShowToast(context: Context): Boolean = reader(context).getBoolean(SHOW_TOAST_KEY, true)

    //Writes to SharedPreferences, but still need to commit setting to save it
    private fun editor(context: Context): SharedPreferences.Editor = context.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE).edit()

    //Reads SharedPreferences value
    private fun reader(context: Context): SharedPreferences = context.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE)

}
