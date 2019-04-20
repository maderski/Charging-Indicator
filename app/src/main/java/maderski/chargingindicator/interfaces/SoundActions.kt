package maderski.chargingindicator.interfaces

import android.content.Intent

interface SoundActions {
    fun connectSound()
    fun disconnectSound()
    fun batteryChargedSound()
    fun playSoundHandler(canPlaySound: Boolean, chosenPlaySound: String)
    fun playConnectSound(batteryStatus: Intent?)
}