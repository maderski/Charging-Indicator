package maderski.chargingindicator.sharedprefs;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Jason on 6/11/16.
 */
public class CIPreferences {

    private static SharedPreferences.Editor _editor;

    private static final String MY_PREFS_NAME = "CIPreferences";

    private static final String VIBRATE_KEY = "vibrate";
    private static final String DISCONNECT_VIBRATE_KEY = "disconnectVibrateKey";
    private static final String DIFF_VIBRATIONS_KEY = "differentVibrationsKey";
    private static final String PLAY_SOUND_KEY = "playSound";
    private static final String DISCONNECT_PLAY_SOUND_KEY = "disconnectPlaySound";
    private static final String BATTERY_CHARGED_PLAY_SOUND_KEY = "batteryChargedPlaySound";
    private static final String SHOW_TOAST_KEY = "showToast";
    private static final String CHOSEN_DISCONNECT_SOUND_KEY = "chosenDisconnectSound";
    private static final String CHOSEN_CONNECT_SOUND_KEY = "chosenConnectSound";
    private static final String CHOSEN_BATTERY_CHARGED_SOUND_KEY = "batteryChargedSound";
    private static final String QUIET_TIME = "quietTime";
    private static final String START_QUIET_TIME = "startQuietTIme";
    private static final String END_QUIET_TIME = "endQuietTime";
    private static final String SHOW_CHARGING_BUBBLE_KEY = "showChargingBubble";
    private static final String CHARGING_BUBBLE_X = "chargingBubbleX";
    private static final String CHARGING_BUBBLE_Y = "chargingBubbleY";


    //Writes to SharedPreferences, but still need to commit setting to save it
    private static SharedPreferences.Editor editor(Context context){

        if(_editor == null){
            _editor = context.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE).edit();
            _editor.apply();
        }

        return _editor;
    }

    //Reads SharedPreferences value
    private static SharedPreferences reader(Context context){

        return context.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
    }

    //Commits write to SharedPreferences
    private static void commit(Context context){
        editor(context).commit();
        _editor = null;
    }

    public static void setChargingBubbleX(Context context, float x) {
        editor(context).putFloat(CHARGING_BUBBLE_X, x);
        commit(context);
    }

    public static float getChargingBubbleX(Context context){
        return reader(context).getFloat(CHARGING_BUBBLE_X, 60f);
    }

    public static void setChargingBubbleY(Context context, float y) {
        editor(context).putFloat(CHARGING_BUBBLE_Y, y);
        commit(context);
    }

    public static float getChargingBubbleY(Context context){
        return reader(context).getFloat(CHARGING_BUBBLE_Y, 20f);
    }

    public static void setShowChargingBubble(Context context, boolean showChargingFab) {
        editor(context).putBoolean(SHOW_CHARGING_BUBBLE_KEY, showChargingFab);
        commit(context);
    }

    public static boolean getShowChargingBubble(Context context){
        return reader(context).getBoolean(SHOW_CHARGING_BUBBLE_KEY, true);
    }

    public static void setStartQuietTime(Context context, int time){
        editor(context).putInt(START_QUIET_TIME, time);
        commit(context);
    }

    public static int getStartQuietTime(Context context){
        return reader(context).getInt(START_QUIET_TIME, 2200);
    }

    public static void setEndQuietTime(Context context, int time){
        editor(context).putInt(END_QUIET_TIME, time);
        commit(context);
    }

    public static int getEndQuietTime(Context context){
        return reader(context).getInt(END_QUIET_TIME, 800);
    }


    public static void setQuietTime(Context context, boolean enabled){
        editor(context).putBoolean(QUIET_TIME, enabled);
        commit(context);
    }

    public static boolean getQuietTime(Context context){
        return reader(context).getBoolean(QUIET_TIME, false);
    }

    public static void setBatteryChargedPlaySound(Context context, boolean enabled){
        editor(context).putBoolean(BATTERY_CHARGED_PLAY_SOUND_KEY, enabled);
        commit(context);
    }

    public static boolean getBatteryChargedPlaySound(Context context){
        return reader(context).getBoolean(BATTERY_CHARGED_PLAY_SOUND_KEY, false);
    }

    public static void setChosenBatteryChargedSound(Context context, String chosenSound){
        editor(context).putString(CHOSEN_BATTERY_CHARGED_SOUND_KEY, chosenSound);
        commit(context);
    }

    public static String getChosenBatteryChargedSound(Context context){
        return reader(context).getString(CHOSEN_BATTERY_CHARGED_SOUND_KEY, "None");
    }

    public static void setChosenDisconnectSound(Context context, String chosenSound){
        editor(context).putString(CHOSEN_DISCONNECT_SOUND_KEY, chosenSound);
        commit(context);
    }

    public static String getChosenDisconnectSound(Context context){
        return reader(context).getString(CHOSEN_DISCONNECT_SOUND_KEY, "None");
    }

    public static void setChosenConnectSound(Context context, String chosenSound){
        editor(context).putString(CHOSEN_CONNECT_SOUND_KEY, chosenSound);
        commit(context);
    }

    public static String getChosenConnectSound(Context context){
        return reader(context).getString(CHOSEN_CONNECT_SOUND_KEY, "None");
    }

    public static void setDisconnectPlaySound(Context context, boolean enabled){
        editor(context).putBoolean(DISCONNECT_PLAY_SOUND_KEY, enabled);
        commit(context);
    }

    public static boolean getDisconnectPlaySound(Context context){
        return reader(context).getBoolean(DISCONNECT_PLAY_SOUND_KEY, false);
    }

    public static void setVibrateOnDisconnect(Context context, boolean enabled){
        editor(context).putBoolean(DISCONNECT_VIBRATE_KEY, enabled);
        commit(context);
    }

    public static boolean getVibrateOnDisconnect(Context context){
        return reader(context).getBoolean(DISCONNECT_VIBRATE_KEY, false);
    }

    public static void setDiffVibrations(Context context, boolean enabled){
        editor(context).putBoolean(DIFF_VIBRATIONS_KEY, enabled);
        commit(context);
    }

    public static boolean getDiffVibrations(Context context){
        return reader(context).getBoolean(DIFF_VIBRATIONS_KEY, true);
    }

    public static void setVibrateWhenPluggedIn(Context context, boolean enabled){
        editor(context).putBoolean(VIBRATE_KEY, enabled);
        commit(context);
    }

    public static boolean GetVibrateWhenPluggedIn(Context context){
        return reader(context).getBoolean(VIBRATE_KEY, false);
    }

    public static void setPlaySound(Context context, boolean enabled){
        editor(context).putBoolean(PLAY_SOUND_KEY, enabled);
        commit(context);
    }

    public static boolean GetPlaySound(Context context){
        return reader(context).getBoolean(PLAY_SOUND_KEY, false);
    }

    public static void setShowToast(Context context, boolean enabled){
        editor(context).putBoolean(SHOW_TOAST_KEY, enabled);
        commit(context);
    }

    public static boolean GetShowToast(Context context){
        return reader(context).getBoolean(SHOW_TOAST_KEY, true);
    }
}
