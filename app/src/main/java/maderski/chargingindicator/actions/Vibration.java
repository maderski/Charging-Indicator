package maderski.chargingindicator.actions;

import android.content.Context;
import android.os.Vibrator;

/**
 * Created by Jason on 9/25/16.
 */

public class Vibration{

    private Vibrator vibrator;

    private int short_buzz = 200;
    private int medium_buzz = 500;
    private int short_gap = 200;
    private int medium_gap = 500;
    private int long_gap = 1000;

    public Vibration(Context context){
        this.vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void standardVibration(){
        long[] pattern = { 0, medium_buzz };
        startVibrate(pattern);
    }

    public void onConnectPattern() {
        long[] pattern = {
                0,  // Start immediately
                short_buzz, short_gap, short_buzz, short_gap, short_buzz
        };
        startVibrate(pattern);
    }

    public void onDisconnectPattern(){
        long[] pattern = {
                0,  // Start immediately
                short_buzz, short_gap, medium_buzz
        };
        startVibrate(pattern);
    }

    private void startVibrate(long[] pattern){
        // Only perform this pattern one time (-1 means "do not repeat")
        vibrator.vibrate(pattern, -1);
    }
}
