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

    //Writes to SharedPreferences, but still need to commit setting to save it
    private static SharedPreferences.Editor editor(Context context){

        if(_editor == null){
            _editor = context.getSharedPreferences(MY_PREFS_NAME, context.MODE_MULTI_PROCESS).edit();
            _editor.commit();
        }

        return _editor;
    }

    //Reads SharedPreferences value
    private static SharedPreferences reader(Context context){

        return context.getSharedPreferences(MY_PREFS_NAME, context.MODE_MULTI_PROCESS);
    }

    //Commits write to SharedPreferences
    private static void commit(Context context){
        editor(context).commit();
        _editor = null;
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
        return reader(context).getBoolean(VIBRATE_KEY, true);
    }
}
