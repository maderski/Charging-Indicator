package maderski.chargingindicator;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;

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

        if(!CIService.isReceiverStarted) {
            Intent intent = new Intent(this, CIService.class);
            startService(intent);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        setButtonPreferences(this);

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
    }

    public void ChangeIconSwitch(View view){
        boolean on = ((Switch) view).isChecked();
        if (on) {
            CIPreferences.SetChangeIcon(this, true);
            Log.i(TAG, "ChangeIconSwitch is ON");
        } else {
            CIPreferences.SetChangeIcon(this, false);
            Log.i(TAG, "ChangeIconSwitch is OFF");
        }
    }

    public void VibrateSwitch(View view){
        boolean on = ((Switch) view).isChecked();
        if (on) {
            CIPreferences.SetVibrateWhenPluggedIn(this, true);
            Log.i(TAG, "VibrateSwitch is ON");
        } else {
            CIPreferences.SetVibrateWhenPluggedIn(this, false);
            Log.i(TAG, "VibrateSwitch is OFF");
        }
    }

    public void PlaySoundSwitch(View view){
        boolean on = ((Switch) view).isChecked();
        if (on) {
            CIPreferences.SetPlaySound(this, true);
            Log.i(TAG, "PlaySoundSwitch is ON");
        } else {
            CIPreferences.SetPlaySound(this, false);
            Log.i(TAG, "PlaySoundSwitch is OFF");
        }
    }

    public void ShowToastSwitch(View view){
        boolean on = ((Switch) view).isChecked();
        if (on) {
            CIPreferences.SetShowToast(this, true);
            Log.i(TAG, "ShowToastSwitch is ON");
        } else {
            CIPreferences.SetShowToast(this, false);
            Log.i(TAG, "ShowToastSwitch is OFF");
        }
    }

    public void ShowNotificationSwitch(View view){
        boolean on = ((Switch) view).isChecked();
        if (on) {
            CIPreferences.SetShowNotification(this, true);
            Log.i(TAG, "ShowNotificationSwitch is ON");
        } else {
            NotificationManager.RemoveNotifMessage(this);
            CIPreferences.SetShowNotification(this, false);
            Log.i(TAG, "ShowNotificationSwitch is OFF");
        }
    }
}
