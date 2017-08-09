package com.yonikal.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.util.Base64;

import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * Created by yoni on 09/08/2017.
 */
public class SharedPrefUtils {

    private static final String SHARED_PREF_NAME = "shared_preferences_file";
    private static Context sContext;

    public static void init(Context context) {
        sContext = context;
    }

    private static <T> boolean saveToSharedPrefPrivate(Context context, Object objectToSave, Class<T> jsonClass, String key) {
        try {
            SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE).edit();
            if (objectToSave instanceof Integer) {
                editor.putInt(key, (int) objectToSave);
            } else if (objectToSave instanceof String) {
                editor.putString(key, (String) objectToSave);
            } else if (objectToSave instanceof Boolean) {
                editor.putBoolean(key, (boolean) objectToSave);
            } else if (objectToSave instanceof Float) {
                editor.putFloat(key, (float) objectToSave);
            }
            if (objectToSave instanceof Parcelable) {
                if (jsonClass == null) {
                    return false;
                }
                String json = new Gson().toJson(objectToSave, jsonClass);
                editor.putString(key, json);
            }
            editor.apply();
            return true;
        } catch (Exception ex) {
            return false;
        }

    }

    public static <T> boolean saveToSharedPref(Context context, Object objectToSave, String key) {
        return saveToSharedPrefPrivate(context, objectToSave, null, key);
    }

    public static <T> boolean saveObjectAsJsonInSharedPref(Context context, Object objectToSave, Class<T> jsonClass, String key) {
        return saveToSharedPrefPrivate(context, objectToSave, jsonClass, key);
    }

    public static <T> Object loadFromSharedPref(Context context, String key, Class<T> jsonClassType) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        if (Integer.class.isAssignableFrom(jsonClassType)) {
            return preferences.getInt(key, 0);
        }
        if (String.class.isAssignableFrom(jsonClassType)) {
            return preferences.getString(key, null);
        }
        if (Boolean.class.isAssignableFrom(jsonClassType)) {
            return preferences.getBoolean(key, false);
        }
        if (Float.class.isAssignableFrom(jsonClassType)) {
            return preferences.getFloat(key, 0);
        }
        return null;
    }

    public static <T> T loadJsonObjectFromSharedPref(Context context, String key, Class<T> jsonClassType) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        String jsonString = preferences.getString(key, null);
        if (jsonString == null) {
            return null;
        }
        return StaticFileUtils.readJsonFromString(context, jsonString, jsonClassType);
    }

    public static SharedPreferences getSharedPreferences(Context context, String name) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public static String readString(Context context, String key, String defaultValue) {
        SharedPreferences prefs = getSharedPreferences(context, SHARED_PREF_NAME);
        return prefs != null ? prefs.getString(key, defaultValue) : defaultValue;
    }

    public static String readString(String key, String defaultValue) {
        SharedPreferences prefs = getSharedPreferences(sContext, SHARED_PREF_NAME);
        return prefs != null ? prefs.getString(key, defaultValue) : defaultValue;
    }

    public static int readInt(Context context, String key, int defaultValue) {
        SharedPreferences prefs = getSharedPreferences(context, SHARED_PREF_NAME);
        return prefs.getInt(key, defaultValue);
    }

    public static int readInt(String key, int defaultValue) {
        SharedPreferences prefs = getSharedPreferences(sContext, SHARED_PREF_NAME);
        return prefs.getInt(key, defaultValue);
    }

    public static long readLong(Context context, String key, long defaultValue) {
        SharedPreferences prefs = getSharedPreferences(context, SHARED_PREF_NAME);
        return prefs.getLong(key, defaultValue);
    }

    public static long readLong(String key, long defaultValue) {
        SharedPreferences prefs = getSharedPreferences(sContext, SHARED_PREF_NAME);
        return prefs.getLong(key, defaultValue);
    }

    public static Boolean readBoolean(Context context, String key, boolean defaultValue) {
        SharedPreferences prefs = getSharedPreferences(context, SHARED_PREF_NAME);
        return prefs == null ? Boolean.valueOf(defaultValue) : Boolean.valueOf(prefs.getBoolean(key, defaultValue));
    }

    public static Boolean readBoolean(String key, boolean defaultValue) {
        SharedPreferences prefs = getSharedPreferences(sContext, SHARED_PREF_NAME);
        return prefs == null ? Boolean.valueOf(defaultValue) : Boolean.valueOf(prefs.getBoolean(key, defaultValue));
    }

    public static Set<String> readStringSet(Context context, String key, Set<String> defaultValue) {
        SharedPreferences prefs = getSharedPreferences(context, SHARED_PREF_NAME);
        return prefs == null ? defaultValue : prefs.getStringSet(key, defaultValue);
    }

    public static Set<String> readStringSet(String key, Set<String> defaultValue) {
        SharedPreferences prefs = getSharedPreferences(sContext, SHARED_PREF_NAME);
        return prefs == null ? defaultValue : prefs.getStringSet(key, defaultValue);
    }

    public static void saveString(Context context, String key, String value) {
        SharedPreferences prefs = getSharedPreferences(context, SHARED_PREF_NAME);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void saveString(String key, String value) {
        SharedPreferences prefs = getSharedPreferences(sContext, SHARED_PREF_NAME);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void saveStringSet(Context context, String key, Set<String> value) {
        SharedPreferences prefs = getSharedPreferences(context, SHARED_PREF_NAME);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet(key, value);
        editor.apply();
    }

    public static void saveStringSet(String key, Set<String> value) {
        SharedPreferences prefs = getSharedPreferences(sContext, SHARED_PREF_NAME);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet(key, value);
        editor.apply();
    }

    public static void saveBoolean(Context context, String key, boolean value) {
        SharedPreferences prefs = getSharedPreferences(context, SHARED_PREF_NAME);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void saveBoolean(String key, boolean value) {
        SharedPreferences prefs = getSharedPreferences(sContext, SHARED_PREF_NAME);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static void saveInt(Context context, String key, int value) {
        SharedPreferences prefs = getSharedPreferences(context, SHARED_PREF_NAME);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static void saveInt(String key, int value) {
        SharedPreferences prefs = getSharedPreferences(sContext, SHARED_PREF_NAME);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static void saveLong(Context context, String key, long value) {
        SharedPreferences prefs = getSharedPreferences(context, SHARED_PREF_NAME);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static void saveLong(String key, long value) {
        SharedPreferences prefs = getSharedPreferences(sContext, SHARED_PREF_NAME);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static void remove(Context context, String key) {
        SharedPreferences prefs = getSharedPreferences(context, SHARED_PREF_NAME);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(key);
        editor.apply();
    }

    public static void remove(String key) {
        SharedPreferences prefs = getSharedPreferences(sContext, SHARED_PREF_NAME);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(key);
        editor.apply();
    }

    public static boolean has(Context context, String name, String key) {
        SharedPreferences prefs = getSharedPreferences(context, name);
        return prefs.contains(key);
    }

    public static boolean has(String name, String key) {
        SharedPreferences prefs = getSharedPreferences(sContext, name);
        return prefs.contains(key);
    }

    public static void saveDate(Context context, String key, Date date) {
        saveLong(context, key, date.getTime());
    }

    public static void saveDate(String key, Date date) {
        saveLong(sContext, key, date.getTime());
    }

    public static Date readDate(Context context, String key) {
        long time = readLong(context, key, (new Date()).getTime());
        return new Date(time);
    }

    public static Date readDate(String key) {
        long time = readLong(sContext, key, (new Date()).getTime());
        return new Date(time);
    }

    public static Date readDate(Context context, String key, Date defaultDate) {
        long time = readLong(context, key, -1L);
        return time == -1L ? defaultDate : new Date(time);
    }

    public static Date readDate(String key, Date defaultDate) {
        long time = readLong(sContext, key, -1L);
        return time == -1L ? defaultDate : new Date(time);
    }

    public static void removeKey(Context context, String key) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.remove(key).commit();
    }

    public static void removeKey(String key) {
        SharedPreferences.Editor editor = sContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.remove(key).apply();
    }

    public static void remove(Context context) {
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE).edit().clear().commit();
    }

    public static void remove() {
        sContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE).edit().clear().commit();
    }

    public static <T extends Serializable> void saveSerializable(Context context, String key, T object) {
        SharedPreferences prefs = getSharedPreferences(context, SHARED_PREF_NAME);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, SharedPrefUtils.ObjectSerializerHelper.objectToString(object));
        editor.apply();
    }

    public static <T extends Serializable> void saveSerializable(String key, T object) {
        SharedPreferences prefs = getSharedPreferences(sContext, SHARED_PREF_NAME);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, SharedPrefUtils.ObjectSerializerHelper.objectToString(object));
        editor.apply();
    }

    public static <T extends Serializable> T readSerializable(Context context, String key, T defaultObject, Class<T> clz) {
        String str = readString(context, key, "");
        if (str.equals("")) {
            return defaultObject;
        } else {
            T object = (T) SharedPrefUtils.ObjectSerializerHelper.stringToObject(str);
            return clz.isInstance(object) ? object : null;
        }
    }

    public static <T extends Serializable> T readSerializable(String key, T defaultObject, Class<T> clz) {
        String str = readString(sContext, key, "");
        if (str.equals("")) {
            return defaultObject;
        } else {
            T object = (T) SharedPrefUtils.ObjectSerializerHelper.stringToObject(str);
            return clz.isInstance(object) ? object : null;
        }
    }

    private static class ObjectSerializerHelper {

        public static String objectToString(Serializable object) {
            String encoded = null;

            try {
                ByteArrayOutputStream e = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(e);
                objectOutputStream.writeObject(object);
                objectOutputStream.close();
                encoded = new String(Base64.encodeToString(e.toByteArray(), 0));
            } catch (IOException e) {
                e.printStackTrace();
            }

            return encoded;
        }

        public static Serializable stringToObject(String string) {
            Serializable object = null;

            try {
                byte[] e = Base64.decode(string, 0);
                ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(e));
                object = (Serializable) objectInputStream.readObject();
            } catch (ClassNotFoundException | ClassCastException | IOException ex) {
                ex.printStackTrace();
            }

            return object;
        }
    }
}
