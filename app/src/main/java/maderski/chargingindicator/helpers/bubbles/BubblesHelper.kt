package maderski.chargingindicator.helpers.bubbles

import com.txusballesteros.bubbles.OnInitializedCallback

interface BubblesHelper : OnInitializedCallback, LastXYCoordListener {
    fun addBubble()
    fun removeBubble()
}