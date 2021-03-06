package maderski.chargingindicator.helpers.bubbles

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import com.txusballesteros.bubbles.BubblesManager
import com.txusballesteros.bubbles.OnInitializedCallback
import maderski.chargingindicator.R
import maderski.chargingindicator.services.BatteryService
import maderski.chargingindicator.sharedprefs.CIPreferences
import maderski.chargingindicator.ui.custom.CustomBubbleLayout

class CIBubblesHelper(private val context: Context) : BubblesHelper {
    // Create BubblesManager Instance
    private val bubblesManager: BubblesManager = BubblesManager.Builder(context)
            .setInitializationCallback(this)
            .build()

    // Once BubblesManager is initialized add the Charging Bolt Bubble
    override fun onInitialized() = addChargingBoltBubble()

    override fun addBubble() = bubblesManager.initialize()

    override fun removeBubble() = bubblesManager.recycle()

    private fun addChargingBoltBubble() {
        val lastXCoord = CIPreferences.getChargingBubbleX(context).toInt()
        val lastYCoord = CIPreferences.getChargingBubbleY(context).toInt()

        val bubbleView: CustomBubbleLayout = LayoutInflater.from(context).inflate(R.layout.floating_widget_layout, null) as CustomBubbleLayout
        bubbleView.setShouldStickToWall(false)
        bubbleView.setLastXYCoordListener(this)

        Log.d(BatteryService.TAG, "LAST XY COORD X: $lastXCoord Y: $lastYCoord")
        bubblesManager.addBubble(bubbleView, lastXCoord, lastYCoord)
    }

    // Save the last XY Coordinate that a user lifted there finger after moving the Bubble
    override fun onLastXYCoord(x: Float, y: Float) {
        Log.d(BatteryService.TAG, "SET LAST XY COORD X: $x Y: $y")
        CIPreferences.setChargingBubbleX(context, x)
        CIPreferences.setChargingBubbleY(context, y)
    }
}