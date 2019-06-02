package maderski.chargingindicator.sharedprefs

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by Jason on 6/11/16.
 */
object CIPreferences : SettingsRepository {
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
    override fun getChosenBatteryChargedSound(context: Context): String = reader(context).getString(CHOSEN_BATTERY_CHARGED_SOUND_KEY, "None")

    override fun setChosenDisconnectSound(context: Context, chosenSound: String) = editor(context).putString(CHOSEN_DISCONNECT_SOUND_KEY, chosenSound).apply()
    override fun getChosenDisconnectSound(context: Context): String = reader(context).getString(CHOSEN_DISCONNECT_SOUND_KEY, "None")

    override fun setChosenConnectSound(context: Context, chosenSound: String) = editor(context).putString(CHOSEN_CONNECT_SOUND_KEY, chosenSound).apply()
    override fun getChosenConnectSound(context: Context): String = reader(context).getString(CHOSEN_CONNECT_SOUND_KEY, "None")

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
