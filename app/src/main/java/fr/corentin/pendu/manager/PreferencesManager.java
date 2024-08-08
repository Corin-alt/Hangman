package fr.corentin.pendu.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;



/**
 * Class {@link PreferencesManager}
 * @author Corentin Dupont
 * @version For project Info0306
 */

public class PreferencesManager {

    private static final int MODE = Context.MODE_PRIVATE;


    public static void saveLanguage(Context context, String fileName, String value){
        SharedPreferences sp = context.getSharedPreferences(fileName, MODE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("lang", value);
        editor.apply();
    }

    public static String getSavedLanguage(Context context, String fileName){
        SharedPreferences sp = context.getSharedPreferences(fileName, MODE);
        return sp.getString("lang", "fr");
    }

    public static void saveObject(Context context, String fileName, String key, Object obj) {
        SharedPreferences sp = context.getSharedPreferences(fileName, MODE);
        SharedPreferences.Editor editor = sp.edit();
        final Gson gson = new Gson();
        String serializedObject = gson.toJson(obj);
        editor.putString(key, serializedObject);
        editor.apply();
    }

    public static <T> T getSavedObject(Context context, String fileName, String key, Class<T> clazz) {
        SharedPreferences sp = context.getSharedPreferences(fileName, MODE);
        if (sp.contains(key)) {
            final Gson gson = new Gson();
            return gson.fromJson(sp.getString(key, ""), clazz);
        }
        return null;
    }
}
