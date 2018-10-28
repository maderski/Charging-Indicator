package maderski.chargingindicator.sharedprefs

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by Jason on 6/11/16.
 */
object CIPreferences {
    private const val MY_PREFS_NAME = "CIPreferences"

    private const val VIBRATE_KEY = "vibrate"
    private const val DISCONNECT_VIBRATE_KEY = "disconnectVibrateKey"
    private const val DIFF_VIBRATIONS_KEY = "differentVibrationsKey"
    private const val PLAY_SOUND_KEY = "playSound"
    private const val DISCONNECT_PLAY_SOUND_KEY = "disconnectPlaySound"
    private const val BATTERY_CHARGED_PLAY_SOUND_KEY = "batteryChargedPlaySound"
    private const val SHOW_TOAST_KEY = "showToast"
    private const val CHOSEN_DISCONNECT_SOUND_KEY = "chosenDisconnectSound"
    private const val CHOSEN_CONNECT_SOUND_KEY = "chosenConnectSound"
    private const val CHOSEN_BATTERY_CHARGED_SOUND_KEY = "batteryChargedSound"
    private const val QUIET_TIME = "quietTime"
    private const val START_QUIET_TIME = "startQuietTIme"
    private const val END_QUIET_TIME = "endQuietTime"
    private const val SHOW_CHARGING_BUBBLE_KEY = "showChargingBubble"
    private const val CHARGING_BUBBLE_X = "chargingBubbleX"
    private const val CHARGING_BUBBLE_Y = "chargingBubbleY"
    private const val BATTERY_CHARGED_PERCENT = "batteryChargedPercent"

    fun setBatteryChargedPercent(context: Context, percent: Int) = editor(context).putInt(BATTERY_CHARGED_PERCENT, percent).apply()
    fun getBatteryCharged(context: Context): Int = reader(context).getInt(BATTERY_CHARGED_PERCENT, 100)

    fun setChargingBubbleX(context: Context, x: Float) = editor(context).putFloat(CHARGING_BUBBLE_X, x).apply()
    fun getChargingBubbleX(context: Context): Float = reader(context).getFloat(CHARGING_BUBBLE_X, 60f)

    fun setChargingBubbleY(context: Context, y: Float) = editor(context).putFloat(CHARGING_BUBBLE_Y, y).apply()
    fun getChargingBubbleY(context: Context): Float = reader(context).getFloat(CHARGING_BUBBLE_Y, 20f)

    fun setShowChargingBubble(context: Context, showChargingFab: Boolean) = editor(context).putBoolean(SHOW_CHARGING_BUBBLE_KEY, showChargingFab).apply()
    fun getShowChargingBubble(context: Context): Boolean = reader(context).getBoolean(SHOW_CHARGING_BUBBLE_KEY, false)

    fun setStartQuietTime(context: Context, time: Int) = editor(context).putInt(START_QUIET_TIME, time).apply()
    fun getStartQuietTime(context: Context): Int = reader(context).getInt(START_QUIET_TIME, 2200)

    fun setEndQuietTime(context: Context, time: Int) = editor(context).putInt(END_QUIET_TIME, time).apply()
    fun getEndQuietTime(context: Context): Int = reader(context).getInt(END_QUIET_TIME, 800)

    fun setQuietTime(context: Context, enabled: Boolean) = editor(context).putBoolean(QUIET_TIME, enabled).apply()
    fun getQuietTime(context: Context): Boolean = reader(context).getBoolean(QUIET_TIME, false)

    fun setBatteryChargedPlaySound(context: Context, enabled: Boolean) = editor(context).putBoolean(BATTERY_CHARGED_PLAY_SOUND_KEY, enabled).apply()
    fun getBatteryChargedPlaySound(context: Context): Boolean = reader(context).getBoolean(BATTERY_CHARGED_PLAY_SOUND_KEY, false)

    fun setChosenBatteryChargedSound(context: Context, chosenSound: String) = editor(context).putString(CHOSEN_BATTERY_CHARGED_SOUND_KEY, chosenSound).apply()
    fun getChosenBatteryChargedSound(context: Context): String = reader(context).getString(CHOSEN_BATTERY_CHARGED_SOUND_KEY, "None")

    fun setChosenDisconnectSound(context: Context, chosenSound: String) = editor(context).putString(CHOSEN_DISCONNECT_SOUND_KEY, chosenSound).apply()
    fun getChosenDisconnectSound(context: Context): String = reader(context).getString(CHOSEN_DISCONNECT_SOUND_KEY, "None")

    fun setChosenConnectSound(context: Context, chosenSound: String) = editor(context).putString(CHOSEN_CONNECT_SOUND_KEY, chosenSound).apply()
    fun getChosenConnectSound(context: Context): String = reader(context).getString(CHOSEN_CONNECT_SOUND_KEY, "None")

    fun setDisconnectPlaySound(context: Context, enabled: Boolean) = editor(context).putBoolean(DISCONNECT_PLAY_SOUND_KEY, enabled).apply()
    fun getDisconnectPlaySound(context: Context): Boolean = reader(context).getBoolean(DISCONNECT_PLAY_SOUND_KEY, false)

    fun setVibrateOnDisconnect(context: Context, enabled: Boolean) = editor(context).putBoolean(DISCONNECT_VIBRATE_KEY, enabled).apply()
    fun getVibrateOnDisconnect(context: Context): Boolean = reader(context).getBoolean(DISCONNECT_VIBRATE_KEY, false)

    fun setDiffVibrations(context: Context, enabled: Boolean) = editor(context).putBoolean(DIFF_VIBRATIONS_KEY, enabled).apply()
    fun getDiffVibrations(context: Context): Boolean = reader(context).getBoolean(DIFF_VIBRATIONS_KEY, true)

    fun setVibrateWhenPluggedIn(context: Context, enabled: Boolean) = editor(context).putBoolean(VIBRATE_KEY, enabled).apply()
    fun getVibrateWhenPluggedIn(context: Context): Boolean = reader(context).getBoolean(VIBRATE_KEY, false)

    fun setPlaySound(context: Context, enabled: Boolean) = editor(context).putBoolean(PLAY_SOUND_KEY, enabled).apply()
    fun getPlaySound(context: Context): Boolean = reader(context).getBoolean(PLAY_SOUND_KEY, false)

    fun setShowToast(context: Context, enabled: Boolean) = editor(context).putBoolean(SHOW_TOAST_KEY, enabled).apply()
    fun getShowToast(context: Context): Boolean = reader(context).getBoolean(SHOW_TOAST_KEY, true)

    //Writes to SharedPreferences, but still need to commit setting to save it
    private fun editor(context: Context): SharedPreferences.Editor = context.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE).edit()

    //Reads SharedPreferences value
    private fun reader(context: Context): SharedPreferences = context.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE)

}
