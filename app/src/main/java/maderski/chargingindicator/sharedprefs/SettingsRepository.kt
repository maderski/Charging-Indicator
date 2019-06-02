package maderski.chargingindicator.sharedprefs

import android.content.Context

interface SettingsRepository {
    fun setBatteryChargedPercent(context: Context, percent: Int)
    fun getBatteryCharged(context: Context): Int
    fun setChargingBubbleX(context: Context, x: Float)
    fun getChargingBubbleX(context: Context): Float
    fun setChargingBubbleY(context: Context, y: Float)
    fun getChargingBubbleY(context: Context): Float
    fun setShowChargingBubble(context: Context, showChargingFab: Boolean)
    fun getShowChargingBubble(context: Context): Boolean
    fun setStartQuietTime(context: Context, time: Int)
    fun getStartQuietTime(context: Context): Int
    fun setEndQuietTime(context: Context, time: Int)
    fun getEndQuietTime(context: Context): Int
    fun setQuietTime(context: Context, enabled: Boolean)
    fun getQuietTime(context: Context): Boolean
    fun setBatteryChargedPlaySound(context: Context, enabled: Boolean)
    fun getBatteryChargedPlaySound(context: Context): Boolean
    fun setChosenBatteryChargedSound(context: Context, chosenSound: String)
    fun getChosenBatteryChargedSound(context: Context): String
    fun setChosenDisconnectSound(context: Context, chosenSound: String)
    fun getChosenDisconnectSound(context: Context): String
    fun setChosenConnectSound(context: Context, chosenSound: String)
    fun getChosenConnectSound(context: Context): String
    fun setDisconnectPlaySound(context: Context, enabled: Boolean)
    fun getDisconnectPlaySound(context: Context): Boolean
    fun setVibrateOnDisconnect(context: Context, enabled: Boolean)
    fun getVibrateOnDisconnect(context: Context): Boolean
    fun setDiffVibrations(context: Context, enabled: Boolean)
    fun getDiffVibrations(context: Context): Boolean
    fun setVibrateWhenPluggedIn(context: Context, enabled: Boolean)
    fun getVibrateWhenPluggedIn(context: Context): Boolean
    fun setPlaySound(context: Context, enabled: Boolean)
    fun getPlaySound(context: Context): Boolean
    fun setShowToast(context: Context, enabled: Boolean)
    fun getShowToast(context: Context): Boolean
}