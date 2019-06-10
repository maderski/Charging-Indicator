package maderski.chargingindicator.ui.fragments

/**
 * Created by Jason on 2/11/17.
 */

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.annotation.StringDef
import androidx.fragment.app.DialogFragment
import android.text.format.DateFormat
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.TimePicker
import maderski.chargingindicator.utils.CITimeUtils
import java.lang.IllegalArgumentException

class TimePickerFragment : androidx.fragment.app.DialogFragment(), TimePickerDialog.OnTimeSetListener {

    private var timeState: String? = null
    private var pickerTitleText: String? = null
    private var previouslySetTime: Int = 0

    private lateinit var dialogListener: TimePickerDialogListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        arguments?.let {
            timeState = it.getString(ARG_PICKER_TIME_STATE)
            previouslySetTime = it.getInt(ARG_PICKER_PREVIOUSLY_SET_TIME)
            pickerTitleText = it.getString(ARG_PICKER_TITLE)
        }
        // get hour and minutes for timePickerDialog
        val hourAndMinutes = CITimeUtils.getHourAndMinutes(previouslySetTime)

        // create a new instance of TimePickerDialog
        val timePickerDialog = TimePickerDialog(activity, this, hourAndMinutes.hour, hourAndMinutes.minutes,
                DateFormat.is24HourFormat(activity))

        pickerTitleText?.let { titleText ->
            // create title
            val tpfTitle = TextView(activity)
            tpfTitle.text = titleText
            tpfTitle.gravity = Gravity.CENTER_HORIZONTAL
            // add title to picker
            timePickerDialog.setCustomTitle(tpfTitle)
        }

        // return TimePickerDialog
        return timePickerDialog
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        // Do something with the time chosen by the user
        val setTime = hourOfDay * 100 + minute
        Log.d(TAG, "Set time: " + Integer.toString(setTime))

        dialogListener.onTimeSet(view, timeState, hourOfDay, minute)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is TimePickerDialogListener) {
            dialogListener = context
        } else {
            throw IllegalArgumentException("Activity must implement TimePickerDialogListener")
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
