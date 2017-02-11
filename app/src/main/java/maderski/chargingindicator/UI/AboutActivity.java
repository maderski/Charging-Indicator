package maderski.chargingindicator.UI;

import android.content.pm.PackageInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import maderski.chargingindicator.R;

public class AboutActivity extends AppCompatActivity {

    private static final String TAG = AboutActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setVersionText();
    }

    //Smooth fade transition from about activity back to the main activity
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(0, R.animator.fadeout);
    }

    //Displays version number
    private void setVersionText(){
        String versionInfo;
        PackageInfo pkgInfo = null;
        TextView tv = (TextView) findViewById(R.id.versionTxt);
        try {
            pkgInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        }catch(Exception e){
            Log.e(TAG, e.getMessage());
        }
        versionInfo = "v" + pkgInfo.versionName;

        tv.setText(versionInfo);
    }
}
