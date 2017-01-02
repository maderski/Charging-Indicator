package maderski.chargingindicator;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import java.util.List;

/*  Created by Jason Maderski
    Date: 12/6/2015

    App gives users with wireless chargers a clearer indicator that the phone is charging
    by creating a notification when Power is connected to the phone.
*/
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkIfCIServiceIsRunning();
    }

    @Override
    protected void onResume(){
        super.onResume();
        setButtonPreferences(this);
        restartBatteryService();
    }

    private void checkIfCIServiceIsRunning(){
        if(!isServiceRunning(CIService.class)){
            Intent serviceIntent = new Intent(this, CIService.class);
            startService(serviceIntent);
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

        Sounds sounds = new Sounds(this);
        sounds.notificationList(this, chosenRingtone, 1);
    }

    public void disconnectSetSound(View view){
        String chosenRingtone = CIPreferences.getChosenDisconnectSound(this);

        Sounds sounds = new Sounds(this);
        sounds.notificationList(this, chosenRingtone, 2);
    }

    public void batteryChargedSetSound(View view){
        String chosenRingtone = CIPreferences.getChosenBatteryChargedSound(this);

        Sounds sounds = new Sounds(this);
        sounds.notificationList(this, chosenRingtone, 3);
    }

    private void setButtonPreferences(Context context){
        Boolean btnState;
        Switch setting_switch;

        btnState = CIPreferences.GetChangeIcon(context);
        setting_switch = (Switch) findViewById(R.id.change_icon_switch);
        setting_switch.setChecked(btnState);

        btnState = CIPreferences.GetVibrateWhenPluggedIn(context);
        setting_switch = (Switch) findViewById(R.id.vibrate_switch);
        setting_switch.setChecked(btnState);

        btnState = CIPreferences.GetPlaySound(context);
        setting_switch = (Switch) findViewById(R.id.play_sound_switch);
        setting_switch.setChecked(btnState);

        btnState = CIPreferences.GetShowToast(context);
        setting_switch = (Switch) findViewById(R.id.show_toast_switch);
        setting_switch.setChecked(btnState);

        btnState = CIPreferences.GetShowNotification(context);
        setting_switch = (Switch) findViewById(R.id.show_notification_switch);
        setting_switch.setChecked(btnState);

        btnState = CIPreferences.GetShowChargingStateIcon(context);
        setting_switch = (Switch) findViewById(R.id.show_chargingstate_switch);
        setting_switch.setChecked(btnState);

        btnState = CIPreferences.getDisconnectPlaySound(context);
        setting_switch = (Switch) findViewById(R.id.disconnect_playsound_switch);
        setting_switch.setChecked(btnState);

        btnState = CIPreferences.getDiffVibrations(context);
        setting_switch = (Switch) findViewById(R.id.diff_vibrations_switch);
        setting_switch.setChecked(btnState);

        btnState = CIPreferences.getVibrateOnDisconnect(context);
        setting_switch = (Switch) findViewById(R.id.disconnect_vibrate_switch);
        setting_switch.setChecked(btnState);

        btnState = CIPreferences.getBatteryChargedPlaySound(context);
        setting_switch = (Switch) findViewById(R.id.battery_charged_sound_switch);
        setting_switch.setChecked(btnState);
    }

    public void batteryChargedSoundSwitch(View view){
        boolean on = ((Switch) view).isChecked();
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

    public void IncreasingDecreasingIconSwitch(View view){
        boolean on = ((Switch) view).isChecked();
        if (on) {
            CIPreferences.setShowChargingStateIcon(this, true);
            Toast.makeText(this, "Show Up/Down Arrow ENABLED", Toast.LENGTH_SHORT).show();
            if(BuildConfig.DEBUG)
                Log.i(TAG, "IncreasingDecreasingIconSwitch is ON");
        } else {
            CIPreferences.setShowChargingStateIcon(this, false);
            if(BuildConfig.DEBUG)
                Log.i(TAG, "IncreasingDecreasingIconSwitch is OFF");
            restartBatteryService();
        }
    }

    public void ChangeIconSwitch(View view){
        boolean on = ((Switch) view).isChecked();
        if (on) {
            CIPreferences.SetChangeIcon(this, true);
            if(BuildConfig.DEBUG)
                Log.i(TAG, "ChangeIconSwitch is ON");
        } else {
            CIPreferences.SetChangeIcon(this, false);
            if(BuildConfig.DEBUG)
                Log.i(TAG, "ChangeIconSwitch is OFF");
        }
    }

    public void VibrateSwitch(View view){
        boolean on = ((Switch) view).isChecked();
        if (on) {
            CIPreferences.SetVibrateWhenPluggedIn(this, true);
            if(BuildConfig.DEBUG)
                Log.i(TAG, "VibrateSwitch is ON");
        } else {
            CIPreferences.SetVibrateWhenPluggedIn(this, false);
            if(BuildConfig.DEBUG)
                Log.i(TAG, "VibrateSwitch is OFF");
        }
    }

    public void DisconnectVibrateSwitch(View view){
        boolean on = ((Switch) view).isChecked();
        if (on) {
            CIPreferences.setVibrateOnDisconnect(this, true);
            if(BuildConfig.DEBUG)
                Log.i(TAG, "DisconnectVibrateSwitch is ON");
        } else {
            CIPreferences.setVibrateOnDisconnect(this, false);
            if(BuildConfig.DEBUG)
                Log.i(TAG, "VibrateSwitch is OFF");
        }
    }

    public void DiffVibrateSwitch(View view){
        boolean on = ((Switch) view).isChecked();
        if (on) {
            CIPreferences.setDiffVibrations(this, true);
            if(BuildConfig.DEBUG)
                Log.i(TAG, "DiffVibrateSwitch is ON");
        } else {
            CIPreferences.setDiffVibrations(this, false);
            if(BuildConfig.DEBUG)
                Log.i(TAG, "DiffVibrateSwitch is OFF");
        }
    }

    public void ConnectSoundSwitch(View view){
        boolean on = ((Switch) view).isChecked();
        if (on) {
            CIPreferences.SetPlaySound(this, true);
            if(BuildConfig.DEBUG)
                Log.i(TAG, "ConnectSoundSwitch is ON");
        } else {
            CIPreferences.SetPlaySound(this, false);
            if(BuildConfig.DEBUG)
                Log.i(TAG, "ConnectSoundSwitch is OFF");
        }
    }

    public void DisconnectSoundSwitch(View view){
        boolean on = ((Switch) view).isChecked();
        if (on) {
            CIPreferences.setDisconnectPlaySound(this, true);
            if(BuildConfig.DEBUG)
                Log.i(TAG, "DisconnectSoundSwitch is ON");
        } else {
            CIPreferences.setDisconnectPlaySound(this, false);
            if(BuildConfig.DEBUG)
                Log.i(TAG, "DisconnectSoundSwitch is OFF");
        }
    }

    public void ShowToastSwitch(View view){
        boolean on = ((Switch) view).isChecked();
        if (on) {
            CIPreferences.SetShowToast(this, true);
            if(BuildConfig.DEBUG)
                Log.i(TAG, "ShowToastSwitch is ON");
        } else {
            CIPreferences.SetShowToast(this, false);
            if(BuildConfig.DEBUG)
                Log.i(TAG, "ShowToastSwitch is OFF");
        }
    }

    public void ShowNotificationSwitch(View view){
        boolean on = ((Switch) view).isChecked();
        if (on) {
            CIPreferences.SetShowNotification(this, true);
            restartBatteryService();

            if(BuildConfig.DEBUG)
                Log.i(TAG, "ShowNotificationSwitch is ON");
        } else {
            PerformActions performActions = new PerformActions(this, new NotificationManager(this, new BatteryManager(this.getIntent())));
            performActions.removeNotification();
            CIPreferences.SetShowNotification(this, false);
            if(BuildConfig.DEBUG)
                Log.i(TAG, "ShowNotificationSwitch is OFF");
        }
    }
    private boolean isServiceRunning(Class<?> serviceClass){
        ActivityManager activityManager = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);

        for (ActivityManager.RunningServiceInfo runningServiceInfo : services) {
            if (runningServiceInfo.service.getClassName().equals(serviceClass.getName())){
                return true;
            }
        }
        return false;
    }

    private void restartBatteryService(){
        Intent serviceIntent = new Intent(this, BatteryService.class);
        if (isServiceRunning(BatteryService.class))
            stopService(serviceIntent);
        startService(serviceIntent);
    }


}
