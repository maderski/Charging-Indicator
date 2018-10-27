package maderski.chargingindicator.ui.activities;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import maderski.chargingindicator.sharedprefs.CIPreferences;
import maderski.chargingindicator.R;
import maderski.chargingindicator.helpers.SoundHelper;
import maderski.chargingindicator.services.CIService;
import maderski.chargingindicator.ui.fragments.TimePickerFragment;
import maderski.chargingindicator.utils.PermissionUtils;
import maderski.chargingindicator.utils.ServiceUtils;

/*  Created by Jason Maderski
    Date: 12/6/2015

    App gives users with wireless chargers a clearer indicator that the phone is charging
    by creating a notification when Power is connected to the phone.
*/
public class MainActivity extends AppCompatActivity implements TimePickerFragment.TimePickerDialogListener {

    private static final String TAG = "MainActivity";

    private EditText mUserChargedPercentEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkIfCIServiceIsRunning();
        initUserChargedPercentEditText();
    }

    @Override
    protected void onResume(){
        super.onResume();
        setButtonPreferences();
    }

    private void initUserChargedPercentEditText() {
        final String userChargedPercent = String.valueOf(CIPreferences.INSTANCE.getBatteryCharged(this));
        mUserChargedPercentEditText = findViewById(R.id.et_user_set_charged_percent);
        mUserChargedPercentEditText.setText(userChargedPercent);
        mUserChargedPercentEditText.setOnClickListener(v -> mUserChargedPercentEditText.setCursorVisible(true));
        mUserChargedPercentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s != null && !s.toString().isEmpty()) {
                    int newValue = Integer.valueOf(s.toString());
                    if(newValue < 100) {
                        CIPreferences.INSTANCE.setBatteryChargedPercent(MainActivity.this, newValue);
                    } else {
                        CIPreferences.INSTANCE.setBatteryChargedPercent(MainActivity.this, 100);
                    }
                }
            }
        });

        mUserChargedPercentEditText.setOnEditorActionListener((v, actionId, event) -> {
            if(actionId == EditorInfo.IME_ACTION_DONE) {
                Log.d(TAG, "DONE");
                InputMethodManager inputMethodManager = (InputMethodManager)MainActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                if(inputMethodManager != null) {
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    mUserChargedPercentEditText.setCursorVisible(false);
                }
                return true;
            } else {
                return false;
            }
        });
    }

    private void checkIfCIServiceIsRunning(){
        boolean isCIServiceRunning = ServiceUtils.isServiceRunning(this, CIService.class);
        if(!isCIServiceRunning) {
            ServiceUtils.startService(this, CIService.class, CIService.TAG);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.action_settings){
            aboutSelected();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Launches the About Activity with about is selected
    private void aboutSelected(){
        Intent i = new Intent(this, AboutActivity.class);
        startActivity(i);
        overridePendingTransition(0, R.animator.fadeout);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){
            Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            if(uri == null) {
                uri = Uri.parse("None");
            }
            switch (requestCode) {
                case 1:
                    CIPreferences.INSTANCE.setChosenConnectSound(this, uri.toString());
                    Log.d(TAG, "Connect Sound Set: " + uri.toString());
                    break;
                case 2:
                    CIPreferences.INSTANCE.setChosenDisconnectSound(this, uri.toString());
                    Log.d(TAG, "Disconnect Sound Set: " + uri.toString());
                    break;
                case 3:
                    CIPreferences.INSTANCE.setChosenBatteryChargedSound(this, uri.toString());
                    Log.d(TAG, "Battery Charged Sound set: " + uri.toString());
                    break;
            }
        }
    }

    public void connectSetSound(View view){
        String chosenRingtone = CIPreferences.INSTANCE.getChosenConnectSound(this);

        SoundHelper soundHelper = new SoundHelper(this);
        soundHelper.notificationList(this, chosenRingtone, 1);
    }

    public void disconnectSetSound(View view){
        String chosenRingtone = CIPreferences.INSTANCE.getChosenDisconnectSound(this);

        SoundHelper soundHelper = new SoundHelper(this);
        soundHelper.notificationList(this, chosenRingtone, 2);
    }

    public void batteryChargedSetSound(View view){
        String chosenRingtone = CIPreferences.INSTANCE.getChosenBatteryChargedSound(this);

        SoundHelper soundHelper = new SoundHelper(this);
        soundHelper.notificationList(this, chosenRingtone, 3);
    }

    public void setStartQuietTime(View view){
        int previousStartTime = CIPreferences.INSTANCE.getStartQuietTime(this);
        DialogFragment timePickerDialog = TimePickerFragment.newInstance(TimePickerFragment.TimeState.START_TIME, previousStartTime, "Start Time");
        timePickerDialog.show(getFragmentManager(), "startQuietTime");
    }

    public void setEndQuietTime(View view){
        int previousStartTime = CIPreferences.INSTANCE.getEndQuietTime(this);
        DialogFragment timePickerDialog = TimePickerFragment.newInstance(TimePickerFragment.TimeState.END_TIME, previousStartTime, "End Time");
        timePickerDialog.show(getFragmentManager(), "EndQuietTime");
    }

    private void setButtonPreferences(){
        Boolean btnState;
        Switch setting_switch;

        btnState = CIPreferences.INSTANCE.getVibrateWhenPluggedIn(this);
        setting_switch = (Switch) findViewById(R.id.vibrate_switch);
        setting_switch.setChecked(btnState);

        btnState = CIPreferences.INSTANCE.getPlaySound(this);
        setting_switch = (Switch) findViewById(R.id.play_sound_switch);
        setting_switch.setChecked(btnState);

        btnState = CIPreferences.INSTANCE.getShowToast(this);
        setting_switch = (Switch) findViewById(R.id.show_toast_switch);
        setting_switch.setChecked(btnState);

        btnState = CIPreferences.INSTANCE.getDisconnectPlaySound(this);
        setting_switch = (Switch) findViewById(R.id.disconnect_playsound_switch);
        setting_switch.setChecked(btnState);

        btnState = CIPreferences.INSTANCE.getDiffVibrations(this);
        setting_switch = (Switch) findViewById(R.id.diff_vibrations_switch);
        setting_switch.setChecked(btnState);

        btnState = CIPreferences.INSTANCE.getVibrateOnDisconnect(this);
        setting_switch = (Switch) findViewById(R.id.disconnect_vibrate_switch);
        setting_switch.setChecked(btnState);

        btnState = CIPreferences.INSTANCE.getBatteryChargedPlaySound(this);
        setting_switch = (Switch) findViewById(R.id.battery_charged_sound_switch);
        setting_switch.setChecked(btnState);

        btnState = CIPreferences.INSTANCE.getQuietTime(this);
        setting_switch = (Switch) findViewById(R.id.quiet_time_switch);
        setting_switch.setChecked(btnState);

        btnState = CIPreferences.INSTANCE.getShowChargingBubble(this);
        setting_switch = (Switch) findViewById(R.id.show_floating_charging_btn_switch);
        setting_switch.setChecked(btnState);
    }


    public void quietTimeSwitch(View view){
        boolean on = ((Switch) view).isChecked();
        CIPreferences.INSTANCE.setQuietTime(this, on);
        Log.d(TAG, "QuietTimeSwitch is enabled: " + Boolean.toString(on));
    }

    public void batteryChargedSoundSwitch(View view){
        boolean on = ((Switch) view).isChecked();
        CIPreferences.INSTANCE.setBatteryChargedPlaySound(this, on);
        Log.d(TAG, "BatteryChargedPlaySoundSwitch is enabled: " + Boolean.toString(on));
    }

    public void vibrateSwitch(View view){
        boolean on = ((Switch) view).isChecked();
        CIPreferences.INSTANCE.setVibrateWhenPluggedIn(this, on);
        Log.d(TAG, "vibrateSwitch is enabled: " + Boolean.toString(on));
    }

    public void disconnectVibrateSwitch(View view){
        boolean on = ((Switch) view).isChecked();
        CIPreferences.INSTANCE.setVibrateOnDisconnect(this, on);
        Log.d(TAG, "disconnectVibrateSwitch is enabled: " + Boolean.toString(on));
    }

    public void diffVibrateSwitch(View view){
        boolean on = ((Switch) view).isChecked();
        CIPreferences.INSTANCE.setDiffVibrations(this, on);
        Log.d(TAG, "diffVibrateSwitch is enabled: " + Boolean.toString(on));
    }

    public void connectSoundSwitch(View view){
        boolean on = ((Switch) view).isChecked();
        CIPreferences.INSTANCE.setPlaySound(this, on);
        Log.d(TAG, "connectSoundSwitch is enabled: " + Boolean.toString(on));
    }

    public void disconnectSoundSwitch(View view){
        boolean on = ((Switch) view).isChecked();
        CIPreferences.INSTANCE.setDisconnectPlaySound(this, on);
        Log.d(TAG, "disconnectSoundSwitch is enabled: " + Boolean.toString(on));
    }

    public void showToastSwitch(View view){
        boolean on = ((Switch) view).isChecked();
        CIPreferences.INSTANCE.setShowToast(this, on);
        Log.d(TAG, "showToastSwitch is enabled: " + Boolean.toString(on));
    }

    @Override
    public void onTimeSet(TimePicker view, String timeState, int hourOfDay, int minute) {
        int timeSet = (hourOfDay * 100) + minute;
        if(timeState.equals(TimePickerFragment.TimeState.START_TIME)){
            Log.d(TAG, "Start time set to: " + Integer.toString(timeSet));
            CIPreferences.INSTANCE.setStartQuietTime(this, timeSet);

        } else if(timeState.equals(TimePickerFragment.TimeState.END_TIME)){
            Log.d(TAG, "End time set to: " + Integer.toString(timeSet));
            CIPreferences.INSTANCE.setEndQuietTime(this, timeSet);
        }
        Toast.makeText(this, "SAVED", Toast.LENGTH_SHORT).show();
    }

    public void floatingChargingBtnSwitch(View view) {
        boolean on = ((Switch) view).isChecked();
        CIPreferences.INSTANCE.setShowChargingBubble(this, on);
        if(on) {
            PermissionUtils.checkSystemOverlayPermission(this);
        }
        Log.d(TAG, "floatingChargingBtnSwitch is enabled: " + Boolean.toString(on));
    }
}
