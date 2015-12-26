package maderski.chargingindicator;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Jason on 12/6/15.
 */
public class CIService extends Service {

    private String TAG = CIService.class.getName();

    //Called by the system every time a client explicitly starts the service by calling startService(Intent)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        //Toast.makeText(getApplication(), "CIService Started", Toast.LENGTH_LONG).show();
        Log.i(TAG, "CIService Started");
        PowerConnectionReceiver pcr = new PowerConnectionReceiver();
        pcr.onReceive(this, intent);
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }
}
