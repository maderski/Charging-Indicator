package maderski.chargingindicator.helpers

import android.content.Context
import android.os.Vibrator

/**
 * Created by Jason on 9/25/16.
 */

class VibrationHelper(context: Context) {

    private val vibrator: Vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    fun standardVibration() {
        val pattern = longArrayOf(0, medium_buzz.toLong())
        startVibrate(pattern)
    }

    fun onConnectPattern() {
        val pattern = longArrayOf(0, // Start immediately
                short_buzz.toLong(), short_gap.toLong(), short_buzz.toLong(), short_gap.toLong(), short_buzz.toLong())
        startVibrate(pattern)
    }

    fun onDisconnectPattern() {
        val pattern = longArrayOf(0, // Start immediately
                short_buzz.toLong(), short_gap.toLong(), medium_buzz.toLong())
        startVibrate(pattern)
    }

    private fun startVibrate(pattern: LongArray) {
        // Only perform this pattern one time (-1 means "do not repeat")
        vibrator.vibrate(pattern, -1)
    }

    companion object {
        private val short_buzz = 200
        private val medium_buzz = 500
        private val short_gap = 200
        private val medium_gap = 500
        private val long_gap = 1000
    }
}
