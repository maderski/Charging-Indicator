package maderski.chargingindicator;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Jason on 6/11/16.
 */
public class CIPreferences {

    private static SharedPreferences.Editor _editor;

    public static String MY_PREFS_NAME = "CIPreferences";

    public static final String CHANGE_ICON_KEY = "changeIcon";
    public static final String VIBRATE_KEY = "vibrate";
    public static final String PLAY_SOUND_KEY = "playSound";
    public static final String SHOW_TOAST_KEY = "showToast";
    public static final String SHOW_NOTIFICATION_KEY = "showNotification";
    public static final String SHOW_INCREASING_DECREASING_KEY = "showIncreasingDecreasing";

    //Writes to SharedPreferences, but still need to commit setting to save it
    private static SharedPreferences.Editor editor(Context context){

        if(_editor == null){
            _editor = context.getSharedPreferences(MY_PREFS_NAME, context.MODE_PRIVATE).edit();
            _editor.commit();
        }

        return _editor;
    }

    //Reads SharedPreferences value
    private static SharedPreferences reader(Context context){

        return context.getSharedPreferences(MY_PREFS_NAME, context.MODE_PRIVATE);
    }

    //Commits write to SharedPreferences
    private static void commit(Context context){
        editor(context).commit();
        _editor = null;
    }

    public static void setShowChargingStateIcon(Context context, boolean enabled){
        editor(context).putBoolean(SHOW_INCREASING_DECREASING_KEY, enabled);
        commit(context);
    }

    public static boolean GetShowChargingStateIcon(Context context){
        return reader(context).getBoolean(SHOW_INCREASING_DECREASING_KEY, true);
    }

    public static void SetChangeIcon(Context context, boolean enabled){
        editor(context).putBoolean(CHANGE_ICON_KEY, enabled);
        commit(context);
    }

    public static boolean GetChangeIcon(Context context){
        return reader(context).getBoolean(CHANGE_ICON_KEY, true);
    }

    public static void SetVibrateWhenPluggedIn(Context context, boolean enabled){
        editor(context).putBoolean(VIBRATE_KEY, enabled);
        commit(context);
    }

    public static boolean GetVibrateWhenPluggedIn(Context context){
        return reader(context).getBoolean(VIBRATE_KEY, false);
    }

    public static void SetPlaySound(Context context, boolean enabled){
        editor(context).putBoolean(PLAY_SOUND_KEY, enabled);
        commit(context);
    }

    public static boolean GetPlaySound(Context context){
        return reader(context).getBoolean(PLAY_SOUND_KEY, false);
    }

    public static void SetShowToast(Context context, boolean enabled){
        editor(context).putBoolean(SHOW_TOAST_KEY, enabled);
        commit(context);
    }

    public static boolean GetShowToast(Context context){
        return reader(context).getBoolean(SHOW_TOAST_KEY, true);
    }

    public static void SetShowNotification(Context context, boolean enabled){
        editor(context).putBoolean(SHOW_NOTIFICATION_KEY, enabled);
        commit(context);
    }

    public static boolean GetShowNotification(Context context){
        return reader(context).getBoolean(SHOW_NOTIFICATION_KEY, true);
    }
}
