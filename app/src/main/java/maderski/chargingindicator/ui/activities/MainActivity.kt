package maderski.chargingindicator.ui.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Switch
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import maderski.chargingindicator.CIApplication
import maderski.chargingindicator.R
import maderski.chargingindicator.actions.interfaces.PerformActions
import maderski.chargingindicator.helpers.permission.CIPermissionHelper
import maderski.chargingindicator.helpers.permission.PermissionHelper
import maderski.chargingindicator.helpers.sound.CISoundHelper
import maderski.chargingindicator.services.BatteryService
import maderski.chargingindicator.services.CIService
import maderski.chargingindicator.sharedprefs.CIPreferences
import maderski.chargingindicator.ui.fragments.TimePickerFragment
import maderski.chargingindicator.utils.PowerUtils
import maderski.chargingindicator.utils.ServiceUtils
import java.lang.Exception
import javax.inject.Inject

/*  Created by Jason Maderski
    Date: 12/6/2015

    App gives users with wireless chargers a clearer indicator that the phone is charging
    by creating a notification when Power is connected to the phone.
*/
class MainActivity : AppCompatActivity(), TimePickerFragment.TimePickerDialogListener {

    @Inject
    lateinit var performActions: PerformActions

    @Inject
    lateinit var permissionHelper: PermissionHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        CIApplication.instance.appComponent.inject(this)
        checkIfCIServiceIsRunning()
        checkIfPhoneIsPluggedIn()
        initUserChargedPercentEditText()
    }

    override fun onResume() {
        super.onResume()
        setButtonPreferences()
    }

    private fun initUserChargedPercentEditText() {
        val userChargedPercent = CIPreferences.getBatteryCharged(this).toString()
        val userChargedPercentEditText: EditText = findViewById(R.id.et_user_set_charged_percent)
        userChargedPercentEditText.setText(userChargedPercent)
        userChargedPercentEditText.setOnClickListener {
            userChargedPercentEditText.isCursorVisible = true
        }
        userChargedPercentEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s != null && !s.toString().isEmpty()) {
                    val newValue = Integer.valueOf(s.toString())
                    if (newValue < 100) {
                        CIPreferences.setBatteryChargedPercent(this@MainActivity, newValue)
                    } else {
                        CIPreferences.setBatteryChargedPercent(this@MainActivity, 100)
                    }
                }
            }
        })

        userChargedPercentEditText.setOnEditorActionListener { view, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                Log.d(TAG, "DONE")
                val inputMethodManager = this@MainActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
                userChargedPercentEditText.isCursorVisible = false
                true
            } else {
                false
            }
        }
    }

    private fun checkIfCIServiceIsRunning() {
        val isCIServiceRunning = ServiceUtils.isServiceRunning(this, CIService::class.java)
        if (!isCIServiceRunning) {
            ServiceUtils.startService(this, CIService::class.java, CIService.TAG)
        }
    }

    private fun checkIfPhoneIsPluggedIn() {
        val isPluggedIn = PowerUtils.isPluggedIn(this)
        val isBatteryServiceRunning = ServiceUtils.isServiceRunning(this, BatteryService::class.java)
        if (isPluggedIn && !isBatteryServiceRunning) {
            ServiceUtils.startService(this, BatteryService::class.java, BatteryService.TAG)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_settings) {
            aboutSelected()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    //Launches the About Activity with about is selected
    private fun aboutSelected() {
        val i = Intent(this, AboutActivity::class.java)
        startActivity(i)
        overridePendingTransition(0, R.animator.fadeout)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode in 1..3 && resultCode == Activity.RESULT_OK && data != null) {
            val uri: Uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
                    ?: Uri.parse("None")
            when (requestCode) {
                1 -> {
                    CIPreferences.setChosenConnectSound(this, uri.toString())
                    Log.d(TAG, "Connect Sound Set: $uri")
                }
                2 -> {
                    CIPreferences.setChosenDisconnectSound(this, uri.toString())
                    Log.d(TAG, "Disconnect Sound Set: $uri")
                }
                3 -> {
                    CIPreferences.setChosenBatteryChargedSound(this, uri.toString())
                    Log.d(TAG, "Battery Charged Sound set: $uri")
                }
            }
        } else if (requestCode == 1000) {

            CIPreferences.setShowChargingBubble(this, true)
            try {
                performActions.showBubble()
            } catch (e: Exception) {
                e.message
            }
        }
    }

    fun connectSetSound(view: View) {
        val chosenRingtone = CIPreferences.getChosenConnectSound(this)

        val soundHelper = CISoundHelper(this)
        soundHelper.notificationList(this, chosenRingtone, 1)
    }

    fun disconnectSetSound(view: View) {
        val chosenRingtone = CIPreferences.getChosenDisconnectSound(this)

        val soundHelper = CISoundHelper(this)
        soundHelper.notificationList(this, chosenRingtone, 2)
    }

    fun batteryChargedSetSound(view: View) {
        val chosenRingtone = CIPreferences.getChosenBatteryChargedSound(this)

        val soundHelper = CISoundHelper(this)
        soundHelper.notificationList(this, chosenRingtone, 3)
    }

    fun setStartQuietTime(view: View) {
        val previousStartTime = CIPreferences.getStartQuietTime(this)
        val timePickerDialog = TimePickerFragment.newInstance(TimePickerFragment.START_TIME, previousStartTime, "Start Time")
        timePickerDialog.show(supportFragmentManager, "startQuietTime")
    }

    fun setEndQuietTime(view: View) {
        val previousStartTime = CIPreferences.getEndQuietTime(this)
        val timePickerDialog = TimePickerFragment.newInstance(TimePickerFragment.END_TIME, previousStartTime, "End Time")
        timePickerDialog.show(supportFragmentManager, "EndQuietTime")
    }

    private fun setButtonPreferences() {
        val vibrateBtnState = CIPreferences.getVibrateWhenPluggedIn(this)
        val vibrateSwitchView = findViewById<Switch>(R.id.vibrate_switch)
        vibrateSwitchView.isChecked = vibrateBtnState

        val playSoundBtnState = CIPreferences.getPlaySound(this)
        val playSoundView = findViewById<Switch>(R.id.play_sound_switch)
        playSoundView.isChecked = playSoundBtnState

        val showToastBtnState = CIPreferences.getShowToast(this)
        val showToastView = findViewById<Switch>(R.id.show_toast_switch)
        showToastView.isChecked = showToastBtnState

        val disconnectSoundBtnState = CIPreferences.getDisconnectPlaySound(this)
        val disconnectSoundView = findViewById<Switch>(R.id.disconnect_playsound_switch)
        disconnectSoundView.isChecked = disconnectSoundBtnState

        val diffVibrationsBtnState = CIPreferences.getDiffVibrations(this)
        val diffVibrationsView = findViewById<Switch>(R.id.diff_vibrations_switch)
        diffVibrationsView.isChecked = diffVibrationsBtnState

        val vibrateOnDisconnectBtnState = CIPreferences.getVibrateOnDisconnect(this)
        val disconnectVibrateView = findViewById<Switch>(R.id.disconnect_vibrate_switch)
        disconnectVibrateView.isChecked = vibrateOnDisconnectBtnState

        val batteryChargedPlaySoundBtnState = CIPreferences.getBatteryChargedPlaySound(this)
        val batteryChargedView = findViewById<Switch>(R.id.battery_charged_sound_switch)
        batteryChargedView.isChecked = batteryChargedPlaySoundBtnState

        val quietTimeBtnState = CIPreferences.getQuietTime(this)
        val quietTimeView = findViewById<Switch>(R.id.quiet_time_switch)
        quietTimeView.isChecked = quietTimeBtnState

        val showChargingBubbleBtnState = CIPreferences.getShowChargingBubble(this)
        val showfloatingChargingButtonView = findViewById<Switch>(R.id.show_floating_charging_btn_switch)
        showfloatingChargingButtonView.isChecked = showChargingBubbleBtnState
        if (showChargingBubbleBtnState) {
            permissionHelper.checkToLaunchSystemOverlaySettings(this)
        }
    }


    fun quietTimeSwitch(view: View) {
        val on = (view as Switch).isChecked
        CIPreferences.setQuietTime(this, on)
        Log.d(TAG, "QuietTimeSwitch is enabled: " + java.lang.Boolean.toString(on))
    }

    fun batteryChargedSoundSwitch(view: View) {
        val on = (view as Switch).isChecked
        CIPreferences.setBatteryChargedPlaySound(this, on)
        Log.d(TAG, "BatteryChargedPlaySoundSwitch is enabled: " + java.lang.Boolean.toString(on))
    }

    fun vibrateSwitch(view: View) {
        val on = (view as Switch).isChecked
        CIPreferences.setVibrateWhenPluggedIn(this, on)
        Log.d(TAG, "vibrateSwitch is enabled: " + java.lang.Boolean.toString(on))
    }

    fun disconnectVibrateSwitch(view: View) {
        val on = (view as Switch).isChecked
        CIPreferences.setVibrateOnDisconnect(this, on)
        Log.d(TAG, "disconnectVibrateSwitch is enabled: " + java.lang.Boolean.toString(on))
    }

    fun diffVibrateSwitch(view: View) {
        val on = (view as Switch).isChecked
        CIPreferences.setDiffVibrations(this, on)
        Log.d(TAG, "diffVibrateSwitch is enabled: " + java.lang.Boolean.toString(on))
    }

    fun connectSoundSwitch(view: View) {
        val on = (view as Switch).isChecked
        CIPreferences.setPlaySound(this, on)
        Log.d(TAG, "connectSoundSwitch is enabled: " + java.lang.Boolean.toString(on))
    }

    fun disconnectSoundSwitch(view: View) {
        val on = (view as Switch).isChecked
        CIPreferences.setDisconnectPlaySound(this, on)
        Log.d(TAG, "disconnectSoundSwitch is enabled: " + java.lang.Boolean.toString(on))
    }

    fun showToastSwitch(view: View) {
        val on = (view as Switch).isChecked
        CIPreferences.setShowToast(this, on)
        Log.d(TAG, "showToastSwitch is enabled: " + java.lang.Boolean.toString(on))
    }

    override fun onTimeSet(view: TimePicker, timeState: String?, hourOfDay: Int, minute: Int) {
        val timeSet = hourOfDay * 100 + minute
        if (timeState == TimePickerFragment.START_TIME) {
            Log.d(TAG, "Start time set to: " + Integer.toString(timeSet))
            CIPreferences.setStartQuietTime(this, timeSet)

        } else if (timeState == TimePickerFragment.END_TIME) {
            Log.d(TAG, "End time set to: " + Integer.toString(timeSet))
            CIPreferences.setEndQuietTime(this, timeSet)
        }
        Toast.makeText(this, "SAVED", Toast.LENGTH_SHORT).show()
    }

    fun floatingChargingBtnSwitch(view: View) {
        val on = (view as Switch).isChecked
        if (on) {
            CIPreferences.setShowChargingBubble(this, true)
            val hasOverlayPerm = permissionHelper.hasOverlayPermission(this)
            val isPluggedIn = PowerUtils.isPluggedIn(this)
            when {
                hasOverlayPerm && isPluggedIn -> {
                    performActions.showBubble()
                }
                !hasOverlayPerm -> {
                    permissionHelper.launchSystemOverlayPermissionSettings(this)
                }
            }
        } else {
            performActions.removeBubble()
            CIPreferences.setShowChargingBubble(this, false)
        }
        Log.d(TAG, "floatingChargingBtnSwitch is enabled: " + java.lang.Boolean.toString(on))
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
