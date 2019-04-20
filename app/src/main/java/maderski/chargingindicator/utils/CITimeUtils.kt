package maderski.chargingindicator.utils

import maderski.chargingindicator.models.HourAndMinutes

object CITimeUtils {
    fun getHourAndMinutes(setTime: Int): HourAndMinutes {
        //Get minutes
        var tempToGetMinutes = setTime
        while (tempToGetMinutes > 60) {
            tempToGetMinutes -= 100
        }
        val minutes = tempToGetMinutes
        //Get hour
        val hour = (setTime - tempToGetMinutes) / 100
        return HourAndMinutes(hour, minutes)
    }
}