package com.yonikal.tools;

import android.content.Context;

import com.google.gson.GsonBuilder;

import java.io.InputStream;

/**
 * Created by yoni on 09/08/2017.
 */
public class StaticFileUtils {

    public static <T> T readJsonFile(Context context, String fileName, Class<T> castingClass) {
        try {
            InputStream inputStream = context.getApplicationContext().getAssets().open(fileName + ".json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            String jsonString = new String(buffer, "UTF-8");
            GsonBuilder gson = new GsonBuilder();
            T jsonObject = gson.create().fromJson(jsonString, castingClass);
            int i = 0;
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T readJsonFromString(Context context, String jsonString, Class<T> castingClass) {
        try {
            GsonBuilder gson = new GsonBuilder();
            T jsonObject = gson.create().fromJson(jsonString, castingClass);
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
