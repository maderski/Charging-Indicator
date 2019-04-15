package maderski.chargingindicator.ui.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
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
import maderski.chargingindicator.R
import maderski.chargingindicator.helpers.SoundHelper
import maderski.chargingindicator.services.BatteryService
import maderski.chargingindicator.services.CIService
import maderski.chargingindicator.sharedprefs.CIPreferences
import maderski.chargingindicator.ui.fragments.TimePickerFragment
import maderski.chargingindicator.utils.PermissionUtils
import maderski.chargingindicator.utils.PowerUtils
import maderski.chargingindicator.utils.ServiceUtils

/*  Created by Jason Maderski
    Date: 12/6/2015

    App gives users with wireless chargers a clearer indicator that the phone is charging
    by creating a notification when Power is connected to the phone.
*/
class MainActivity : AppCompatActivity(), TimePickerFragment.TimePickerDialogListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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

        if (resultCode == Activity.RESULT_OK && data != null) {
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
        }
    }

    fun connectSetSound(view: View) {
        val chosenRingtone = CIPreferences.getChosenConnectSound(this)

        val soundHelper = SoundHelper(this)
        soundHelper.notificationList(this, chosenRingtone, 1)
    }

    fun disconnectSetSound(view: View) {
        val chosenRingtone = CIPreferences.getChosenDisconnectSound(this)

        val soundHelper = SoundHelper(this)
        soundHelper.notificationList(this, chosenRingtone, 2)
    }

    fun batteryChargedSetSound(view: View) {
        val chosenRingtone = CIPreferences.getChosenBatteryChargedSound(this)

        val soundHelper = SoundHelper(this)
        soundHelper.notificationList(this, chosenRingtone, 3)
    }

    fun setStartQuietTime(view: View) {
        val previousStartTime = CIPreferences.getStartQuietTime(this)
        val timePickerDialog = TimePickerFragment.newInstance(TimePickerFragment.START_TIME, previousStartTime, "Start Time")
        timePickerDialog.show(fragmentManager, "startQuietTime")
    }

    fun setEndQuietTime(view: View) {
        val previousStartTime = CIPreferences.getEndQuietTime(this)
        val timePickerDialog = TimePickerFragment.newInstance(TimePickerFragment.END_TIME, previousStartTime, "End Time")
        timePickerDialog.show(fragmentManager, "EndQuietTime")
    }

    private fun setButtonPreferences() {
        var btnState: Boolean?
        var setting_switch: Switch

        btnState = CIPreferences.getVibrateWhenPluggedIn(this)
        setting_switch = findViewById<View>(R.id.vibrate_switch) as Switch
        setting_switch.isChecked = btnState

        btnState = CIPreferences.getPlaySound(this)
        setting_switch = findViewById<View>(R.id.play_sound_switch) as Switch
        setting_switch.isChecked = btnState

        btnState = CIPreferences.getShowToast(this)
        setting_switch = findViewById<View>(R.id.show_toast_switch) as Switch
        setting_switch.isChecked = btnState

        btnState = CIPreferences.getDisconnectPlaySound(this)
        setting_switch = findViewById<View>(R.id.disconnect_playsound_switch) as Switch
        setting_switch.isChecked = btnState

        btnState = CIPreferences.getDiffVibrations(this)
        setting_switch = findViewById<View>(R.id.diff_vibrations_switch) as Switch
        setting_switch.isChecked = btnState

        btnState = CIPreferences.getVibrateOnDisconnect(this)
        setting_switch = findViewById<View>(R.id.disconnect_vibrate_switch) as Switch
        setting_switch.isChecked = btnState

        btnState = CIPreferences.getBatteryChargedPlaySound(this)
        setting_switch = findViewById<View>(R.id.battery_charged_sound_switch) as Switch
        setting_switch.isChecked = btnState

        btnState = CIPreferences.getQuietTime(this)
        setting_switch = findViewById<View>(R.id.quiet_time_switch) as Switch
        setting_switch.isChecked = btnState

        btnState = CIPreferences.getShowChargingBubble(this)
        setting_switch = findViewById<View>(R.id.show_floating_charging_btn_switch) as Switch
        setting_switch.isChecked = btnState
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
        CIPreferences.setShowChargingBubble(this, on)
        if (on) {
            PermissionUtils.checkSystemOverlayPermission(this)
        }
        Log.d(TAG, "floatingChargingBtnSwitch is enabled: " + java.lang.Boolean.toString(on))
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
