package maderski.chargingindicator.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.txusballesteros.bubbles.BubbleLayout
import maderski.chargingindicator.interfaces.LastXYCoordListener

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

    fun setLastXYCoordListener(lastXYCoordListener: LastXYCoordListener) { mLastXYCoordListener = lastXYCoordListener }

    override fun onTouch(v: View?, motionEvent: MotionEvent?): Boolean {
        motionEvent?.let {
            return when(it.action) {
                MotionEvent.ACTION_DOWN -> {
                    Log.d(TAG, "MotionEvent ACTION_DOWN")
                    mDownRawX = it.rawX
                    mDownRawY = it.rawY

                    false
                }

                MotionEvent.ACTION_UP -> {
                    Log.d(TAG, "MotionEvent ACTION_UP")
                    val upRawX = it.rawX
                    val upRawY = it.rawY

                    val upDX = upRawX - mDownRawX
                    val upDY = upRawY - mDownRawY

                    if(Math.abs(upDX) < DRAG_THREASHOLD && Math.abs(upDY) < DRAG_THREASHOLD) {
                        return performClick()
                    }

                    if(mLastXYCoordListener != null) {
                        mLastXYCoordListener?.onLastXYCoord(upRawX, upRawY)
                    }
                    Log.d(TAG, "X: ${upRawX} Y: ${upRawY}")

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