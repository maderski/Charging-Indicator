package maderski.chargingindicator.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.txusballesteros.bubbles.BubbleLayout
import maderski.chargingindicator.helpers.bubbles.LastXYCoordListener

class CustomBubbleLayout : BubbleLayout, View.OnTouchListener {
    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr)

    private var mLastXYCoordListener: LastXYCoordListener? = null

    private var mDownRawX = 0f
    private var mDownRawY = 0f

    init {
        setOnTouchListener(this)
    }

    fun setLastXYCoordListener(lastXYCoordListener: LastXYCoordListener) {
        mLastXYCoordListener = lastXYCoordListener
    }

    override fun onTouch(v: View?, motionEvent: MotionEvent?): Boolean {
        motionEvent?.let { event ->
            return when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    Log.d(TAG, "MotionEvent ACTION_DOWN")
                    mDownRawX = event.rawX
                    mDownRawY = event.rawY

                    false
                }

                MotionEvent.ACTION_UP -> {
                    Log.d(TAG, "MotionEvent ACTION_UP")
                    val upRawX = event.rawX
                    val upRawY = event.rawY

                    val upDX = upRawX - mDownRawX
                    val upDY = upRawY - mDownRawY

                    if (Math.abs(upDX) < DRAG_THREASHOLD && Math.abs(upDY) < DRAG_THREASHOLD) {
                        return performClick()
                    }

                    v?.let {
                        val width = it.width
                        val height = it.height
                        val xCenter: Float = upRawX - (width / 1.75f)
                        val yCenter: Float = upRawY - (height / 1.5f)

                        if (mLastXYCoordListener != null) {
                            mLastXYCoordListener?.onLastXYCoord(xCenter, yCenter)
                        }
                        Log.d(TAG, "X: $xCenter Y: $yCenter W: $width H: $height")
                    }

                    false
                }

                else -> {
                    super.onTouchEvent(motionEvent)
                }
            }
        }
        return super.onTouchEvent(motionEvent)
    }

    companion object {
        const val TAG = "CustomBubbleLayout"
        private const val DRAG_THREASHOLD = 20
    }
}