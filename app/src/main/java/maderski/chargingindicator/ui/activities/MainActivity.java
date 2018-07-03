package maderski.chargingindicator.ui.activities;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import maderski.chargingindicator.BuildConfig;
import maderski.chargingindicator.sharedprefs.CIPreferences;
import maderski.chargingindicator.R;
import maderski.chargingindicator.actions.Sound;
import maderski.chargingindicator.services.CIService;
import maderski.chargingindicator.ui.fragments.TimePickerFragment;
import maderski.chargingindicator.utils.PermissionUtils;
import maderski.chargingindicator.utils.ServiceUtils;

/*  Created by Jason Maderski
    Date: 12/6/2015

    App gives users with wireless chargers a clearer indicator that the phone is charging
    by creating a notification when Power is connected to the phone.
*/
public class MainActivity extends Activity implements TimePickerFragment.TimePickerDialogListener {

    private static final String TAG = MainActivity.class.getName();

    private FirebaseAnalytics mFirebaseAnalytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkIfCIServiceIsRunning();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        setButtonPreferences();
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

    @Override
    protected void onDestroy(){
        super.onDestroy();
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
            if(requestCode == 1) {
                Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                if(uri == null)
                    uri = Uri.parse("None");
                CIPreferences.setChosenConnectSound(this, uri.toString());
                if (BuildConfig.DEBUG) {
                    Log.i(TAG, uri.toString());
                    Log.i(TAG, "Connect Sound Set: " + uri.toString());
                }
            }else if(requestCode == 2){
                Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                if(uri == null)
                    uri = Uri.parse("None");
                CIPreferences.setChosenDisconnectSound(this, uri.toString());
                if (BuildConfig.DEBUG) {
                    Log.i(TAG, uri.toString());
                    Log.i(TAG, "Disconnect Sound Set: " + uri.toString());
                }
            }else if(requestCode == 3){
                Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                if(uri == null)
                    uri = Uri.parse("None");
                CIPreferences.setChosenBatteryChargedSound(this, uri.toString());
                if(BuildConfig.DEBUG){
                    Log.i(TAG, uri.toString());
                    Log.i(TAG, "Battery Charged Sound set: " + uri.toString());
                }
            }
        }
    }

    public void connectSetSound(View view){
        String chosenRingtone = CIPreferences.getChosenConnectSound(this);

        Sound sound = new Sound(this);
        sound.notificationList(this, chosenRingtone, 1);
    }

    public void disconnectSetSound(View view){
        String chosenRingtone = CIPreferences.getChosenDisconnectSound(this);

        Sound sound = new Sound(this);
        sound.notificationList(this, chosenRingtone, 2);
    }

    public void batteryChargedSetSound(View view){
        String chosenRingtone = CIPreferences.getChosenBatteryChargedSound(this);

        Sound sound = new Sound(this);
        sound.notificationList(this, chosenRingtone, 3);
    }

    public void setQuietTimes(View view){
        Log.d(TAG, "QuietTime button pressed");

    }

    public void setStartQuietTime(View view){
        int previousStartTime = CIPreferences.getStartQuietTime(this);
        DialogFragment timePickerDialog = TimePickerFragment.newInstance(TimePickerFragment.TimeState.START_TIME, previousStartTime, "Start Time");
        timePickerDialog.show(getFragmentManager(), "startQuietTime");
    }

    public void setEndQuietTime(View view){
        int previousStartTime = CIPreferences.getEndQuietTime(this);
        DialogFragment timePickerDialog = TimePickerFragment.newInstance(TimePickerFragment.TimeState.END_TIME, previousStartTime, "End Time");
        timePickerDialog.show(getFragmentManager(), "EndQuietTime");
    }

    private void setButtonPreferences(){
        Boolean btnState;
        Switch setting_switch;

        btnState = CIPreferences.GetChangeIcon(this);
        setting_switch = (Switch) findViewById(R.id.change_icon_switch);
        setting_switch.setChecked(btnState);

        btnState = CIPreferences.GetVibrateWhenPluggedIn(this);
        setting_switch = (Switch) findViewById(R.id.vibrate_switch);
        setting_switch.setChecked(btnState);

        btnState = CIPreferences.GetPlaySound(this);
        setting_switch = (Switch) findViewById(R.id.play_sound_switch);
        setting_switch.setChecked(btnState);

        btnState = CIPreferences.GetShowToast(this);
        setting_switch = (Switch) findViewById(R.id.show_toast_switch);
        setting_switch.setChecked(btnState);

        btnState = CIPreferences.GetShowNotification(this);
        setting_switch = (Switch) findViewById(R.id.show_notification_switch);
        setting_switch.setChecked(btnState);

        btnState = CIPreferences.GetShowChargingStateIcon(this);
        setting_switch = (Switch) findViewById(R.id.show_chargingstate_switch);
        setting_switch.setChecked(btnState);

        btnState = CIPreferences.getDisconnectPlaySound(this);
        setting_switch = (Switch) findViewById(R.id.disconnect_playsound_switch);
        setting_switch.setChecked(btnState);

        btnState = CIPreferences.getDiffVibrations(this);
        setting_switch = (Switch) findViewById(R.id.diff_vibrations_switch);
        setting_switch.setChecked(btnState);

        btnState = CIPreferences.getVibrateOnDisconnect(this);
        setting_switch = (Switch) findViewById(R.id.disconnect_vibrate_switch);
        setting_switch.setChecked(btnState);

        btnState = CIPreferences.getBatteryChargedPlaySound(this);
        setting_switch = (Switch) findViewById(R.id.battery_charged_sound_switch);
        setting_switch.setChecked(btnState);

        btnState = CIPreferences.getQuietTime(this);
        setting_switch = (Switch) findViewById(R.id.quiet_time_switch);
        setting_switch.setChecked(btnState);

        btnState = CIPreferences.getShowChargingBubble(this);
        setting_switch = (Switch) findViewById(R.id.show_floating_charging_btn_switch);
        setting_switch.setChecked(btnState);
    }

    public void setFirebaseSwitchEvent(String eventName, boolean switchedOn){
        Bundle bundle = new Bundle();
        bundle.putInt(FirebaseAnalytics.Param.VALUE, switchedOn ? 1 : 0);
        mFirebaseAnalytics.logEvent(eventName, bundle);
    }

    public void quietTimeSwitch(View view){
        boolean on = ((Switch) view).isChecked();
        setFirebaseSwitchEvent("quiet_time", on);
        if (on) {
            CIPreferences.setQuietTime(this, true);
            if(BuildConfig.DEBUG)
                Log.i(TAG, "QuietTimeSwitch is ON");
        } else {
            CIPreferences.setQuietTime(this, false);
            if(BuildConfig.DEBUG)
                Log.i(TAG, "QuietTimeSwitch is OFF");
        }
    }

    public void batteryChargedSoundSwitch(View view){
        boolean on = ((Switch) view).isChecked();
        setFirebaseSwitchEvent("charged_sound", on);
        if (on) {
            CIPreferences.setBatteryChargedPlaySound(this, true);
            if(BuildConfig.DEBUG)
                Log.i(TAG, "BatteryChargedPlaySoundSwitch is ON");
        } else {
            CIPreferences.setBatteryChargedPlaySound(this, false);
            if(BuildConfig.DEBUG)
                Log.i(TAG, "BatteryChargedPlaySoundSwitch is OFF");
        }
    }

    public void increasingDecreasingIconSwitch(View view){
        boolean on = ((Switch) view).isChecked();
        setFirebaseSwitchEvent("show_charging_state_icon", on);
        if (on) {
            CIPreferences.setShowChargingStateIcon(this, true);
            Toast.makeText(this, "Show Up/Down Arrow ENABLED", Toast.LENGTH_SHORT).show();
            if(BuildConfig.DEBUG)
                Log.i(TAG, "increasingDecreasingIconSwitch is ON");
        } else {
            CIPreferences.setShowChargingStateIcon(this, false);
            if(BuildConfig.DEBUG)
                Log.i(TAG, "increasingDecreasingIconSwitch is OFF");
        }
    }

    public void changeIconSwitch(View view){
        boolean on = ((Switch) view).isChecked();
        setFirebaseSwitchEvent("change_icon", on);
        if (on) {
            CIPreferences.setChangeIcon(this, true);
            if(BuildConfig.DEBUG)
                Log.i(TAG, "changeIconSwitch is ON");
        } else {
            CIPreferences.setChangeIcon(this, false);
            if(BuildConfig.DEBUG)
                Log.i(TAG, "changeIconSwitch is OFF");
        }
    }

    public void vibrateSwitch(View view){
        boolean on = ((Switch) view).isChecked();
        if (on) {
            CIPreferences.setVibrateWhenPluggedIn(this, true);
            if(BuildConfig.DEBUG)
                Log.i(TAG, "vibrateSwitch is ON");
        } else {
            CIPreferences.setVibrateWhenPluggedIn(this, false);
            if(BuildConfig.DEBUG)
                Log.i(TAG, "vibrateSwitch is OFF");
        }
    }

    public void disconnectVibrateSwitch(View view){
        boolean on = ((Switch) view).isChecked();
        setFirebaseSwitchEvent("disconnect_vibrate", on);
        if (on) {
            CIPreferences.setVibrateOnDisconnect(this, true);
            if(BuildConfig.DEBUG)
                Log.i(TAG, "disconnectVibrateSwitch is ON");
        } else {
            CIPreferences.setVibrateOnDisconnect(this, false);
            if(BuildConfig.DEBUG)
                Log.i(TAG, "vibrateSwitch is OFF");
        }
    }

    public void diffVibrateSwitch(View view){
        boolean on = ((Switch) view).isChecked();
        setFirebaseSwitchEvent("diff_vibrate", on);
        if (on) {
            CIPreferences.setDiffVibrations(this, true);
            if(BuildConfig.DEBUG)
                Log.i(TAG, "diffVibrateSwitch is ON");
        } else {
            CIPreferences.setDiffVibrations(this, false);
            if(BuildConfig.DEBUG)
                Log.i(TAG, "diffVibrateSwitch is OFF");
        }
    }

    public void connectSoundSwitch(View view){
        boolean on = ((Switch) view).isChecked();
        setFirebaseSwitchEvent("connect_sound", on);
        if (on) {
            CIPreferences.setPlaySound(this, true);
            if(BuildConfig.DEBUG)
                Log.i(TAG, "connectSoundSwitch is ON");
        } else {
            CIPreferences.setPlaySound(this, false);
            if(BuildConfig.DEBUG)
                Log.i(TAG, "connectSoundSwitch is OFF");
        }
    }

    public void disconnectSoundSwitch(View view){
        boolean on = ((Switch) view).isChecked();
        setFirebaseSwitchEvent("disconnect_sound", on);
        if (on) {
            CIPreferences.setDisconnectPlaySound(this, true);
            if(BuildConfig.DEBUG)
                Log.i(TAG, "disconnectSoundSwitch is ON");
        } else {
            CIPreferences.setDisconnectPlaySound(this, false);
            if(BuildConfig.DEBUG)
                Log.i(TAG, "disconnectSoundSwitch is OFF");
        }
    }

    public void showToastSwitch(View view){
        boolean on = ((Switch) view).isChecked();
        setFirebaseSwitchEvent("show_toast", on);
        if (on) {
            CIPreferences.setShowToast(this, true);
            if(BuildConfig.DEBUG)
                Log.i(TAG, "showToastSwitch is ON");
        } else {
            CIPreferences.setShowToast(this, false);
            if(BuildConfig.DEBUG)
                Log.i(TAG, "showToastSwitch is OFF");
        }
    }

    public void showNotificationSwitch(View view){
        boolean on = ((Switch) view).isChecked();
        setFirebaseSwitchEvent("show_notification", on);
        if (on) {
            CIPreferences.setShowNotification(this, true);

            if(BuildConfig.DEBUG)
                Log.i(TAG, "showNotificationSwitch is ON");
        } else {
            CIPreferences.setShowNotification(this, false);
            if(BuildConfig.DEBUG)
                Log.i(TAG, "showNotificationSwitch is OFF");
        }
    }

    @Override
    public void onTimeSet(TimePicker view, String timeState, int hourOfDay, int minute) {
        int timeSet = (hourOfDay * 100) + minute;
        if(timeState.equals(TimePickerFragment.TimeState.START_TIME)){
            Log.d(TAG, "Start time set to: " + Integer.toString(timeSet));
            CIPreferences.setStartQuietTime(this, timeSet);

        } else if(timeState.equals(TimePickerFragment.TimeState.END_TIME)){
            Log.d(TAG, "End time set to: " + Integer.toString(timeSet));
            CIPreferences.setEndQuietTime(this, timeSet);
        }
        Toast.makeText(this, "SAVED", Toast.LENGTH_SHORT).show();
    }

    public void floatingChargingBtnSwitch(View view) {
        boolean on = ((Switch) view).isChecked();
        setFirebaseSwitchEvent("floating_charging_button", on);
        CIPreferences.setShowChargingBubble(this, on);
        if(on) {
            PermissionUtils.checkSystemOverlayPermission(this);
        }

        if(BuildConfig.DEBUG) {
            String switchState = on ? "ON" : "OFF";
            Log.d(TAG, "floatingChargingBtnSwitch is " + switchState);
        }
    }
}
