package maderski.chargingindicator.ui.fragments

/**
 * Created by Jason on 2/11/17.
 */

import android.app.Dialog
import android.app.DialogFragment
import android.app.TimePickerDialog
import android.os.Bundle
import android.support.annotation.StringDef
import android.text.format.DateFormat
import android.util.Log
import android.view.Gravity
import android.widget.TextView
import android.widget.TimePicker

class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    private var mTimeState: String? = null
    private var dialogListener: TimePickerDialogListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        mTimeState = arguments.getString(ARG_PICKER_TIME_STATE)
        val previouslySetTime = arguments.getInt(ARG_PICKER_PREVIOUSLY_SET_TIME)
        val pickerTitle = arguments.getString(ARG_PICKER_TITLE)
        dialogListener = if (activity is TimePickerDialogListener) activity as TimePickerDialogListener else null

        //Create and set Title
        val tpfTitle = TextView(activity)
        tpfTitle.text = pickerTitle
        tpfTitle.gravity = Gravity.CENTER_HORIZONTAL

        //Get minutes
        var tempToGetMinutes = previouslySetTime
        while (tempToGetMinutes > 60) {
            tempToGetMinutes -= 100
        }
        val minute = tempToGetMinutes
        //Get hour
        val hour = (previouslySetTime - tempToGetMinutes) / 100

        // Create a new instance of TimePickerDialog
        val timePickerDialog = TimePickerDialog(activity, this, hour, minute,
                DateFormat.is24HourFormat(activity))
        //Add title to picker
        timePickerDialog.setCustomTitle(tpfTitle)
        //Return TimePickerDialog
        return timePickerDialog
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        // Do something with the time chosen by the user
        val setTime = hourOfDay * 100 + minute
        Log.d(TAG, "Set time: " + Integer.toString(setTime))

        if (dialogListener != null) {
            dialogListener!!.onTimeSet(view, mTimeState, hourOfDay, minute)
        }
    }

    interface TimePickerDialogListener {
        fun onTimeSet(view: TimePicker, timeState: String?, hourOfDay: Int, minute: Int)
    }

    companion object {
        private const val TAG = "TimePickerFragment"

        @StringDef(START_TIME, END_TIME)
        @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
        annotation class TimeState

        const val START_TIME = "start_time"
        const val END_TIME = "end_time"

        const val ARG_PICKER_TIME_STATE = "picker_time_state"
        const val ARG_PICKER_PREVIOUSLY_SET_TIME = "picker_previouslySetTime"
        const val ARG_PICKER_TITLE = "picker_title"

        fun newInstance(@TimeState timeState: String, previouslySetTime: Int, title: String): TimePickerFragment {
            val args = Bundle()
            args.putString(ARG_PICKER_TIME_STATE, timeState)
            args.putInt(ARG_PICKER_PREVIOUSLY_SET_TIME, previouslySetTime)
            args.putString(ARG_PICKER_TITLE, title)
            val fragment = TimePickerFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
