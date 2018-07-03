package maderski.chargingindicator.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView


class MovableFloatingImageView : ImageView, View.OnTouchListener {
    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr)

    private var downRawX: Float = 0f
    private var downRawY: Float = 0f
    private var downDX: Float = 0f
    private var downDY: Float = 0f

    init {
        setOnTouchListener(this)
    }

    override fun onTouch(view: View, motionEvent: MotionEvent?): Boolean {
        motionEvent?.let {
            val action = it.action
            return when(action) {
                MotionEvent.ACTION_DOWN -> {
                    Log.d(TAG, "MotionEvent ACTION_DOWN")
                    downRawX = it.rawX
                    downRawY = it.rawY

                    downDX = view.x - downRawX
                    downDY = view.y - downRawY

                    true
                }

                MotionEvent.ACTION_MOVE -> {
                    Log.d(TAG, "MotionEvent ACTION_MOVE")
                    // ImageView Dimensions
                    val viewWidth = view.width
                    val viewHeight = view.height

                    // ParentView Dimensions
                    val viewParent = view.parent as View
                    val parentWidth = viewParent.width
                    val parentHeight = viewParent.height

                    var newX = it.rawX + downDX
                    // Don't allow the ImageView past the left hand side of the parent
                    newX = Math.max(0f, newX)
                    // Difference between the parent height and the view height
                    val dW = (parentWidth - viewWidth).toFloat()
                    // Don't allow the ImageView past the right hand side of the parent
                    newX = Math.min(dW, newX)

                    var newY = it.rawY + downDY
                    // Don't allow the ImageView past the top of the parent
                    newY = Math.max(0f, newY)
                    // Difference between the parent height and the view height
                    val dH = (parentHeight - viewHeight).toFloat()
                    // Don't allow the ImageView past the bottom of the parent
                    newY = Math.min(dH, newY)

                    view.animate()
                            .x(newX)
                            .y(newY)
                            .setDuration(0)
                            .start()

                    true
                }

                MotionEvent.ACTION_UP -> {
                    Log.d(TAG, "MotionEvent ACTION_UP")
                    val upRawX = it.rawX
                    val upRawY = it.rawY

                    val upDX = upRawX - downRawX
                    val upDY = upRawY - downRawY

                    if(Math.abs(upDX) < DRAG_THREASHOLD && Math.abs(upDY) < DRAG_THREASHOLD) {
                        return performClick()
                    }

                    true
                }

                else -> {
                    Log.d(TAG, "MotionEvent UNKNOWN")
                    super.onTouchEvent(motionEvent)
                }
            }
        }
        Log.d(TAG, "MotionEvent NULL")
        return super.onTouchEvent(motionEvent)
    }

    companion object {
        private const val TAG = "MovableFloatingImageVie"
        private const val DRAG_THREASHOLD = 20
    }

}