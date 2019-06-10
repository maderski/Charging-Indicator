package maderski.chargingindicator.helpers.vibration

import android.os.Vibrator

interface VibrationHelper {
    val vibrator: Vibrator
    fun standardVibration()
    fun onConnectPattern()
    fun onDisconnectPattern()
    fun startVibrate(pattern: LongArray)
}