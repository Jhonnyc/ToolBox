package com.yonikal.tools.utils;

import android.util.Log;

import java.security.MessageDigest;

/**
 * Created by yoni on 10/08/2017.
 */
public class HashUtil {

    private static final String TAG = HashUtil.class.getSimpleName();
    private static final String MD5 = "MD5";

    public static String hashString(String unhashed) {
        String hashed = null;
        try {
            MessageDigest md = MessageDigest.getInstance(MD5);
            md.reset();
            md.update(unhashed.getBytes());
            byte[] data = md.digest();
            StringBuffer hex = new StringBuffer();
            for (int i = 0; i < data.length; i++) {
                hex.append(Integer.toHexString((data[i] & 0xFF) | 0x100)).substring(1, 3);
            }
            hashed = hex.toString();
        } catch (Exception e) {
            Log.e(TAG, "Failed to hash string : " + unhashed + "; Cause - " + e.getMessage());
        }
        return hashed;
    }
}
