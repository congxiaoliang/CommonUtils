package com.cxl.commonutils;

import android.util.Log;

/**
 * Created by congxiaoliang on 15/5/28.
 */
public class StringUtils {
    private static final String TAG = "StringUtils";
    private static boolean DEBUG = ConfigFeature.DEBUG;

    /**
     * check the String is empty or not
     * @param str The target String. can be null.
     * @return empty or not
     */
    public static boolean isEmpty(String str) {
        if (null == str) {
            return true;
        }
        return str.isEmpty();
    }


    public static int parseInt(String s, int def) {
        if (isEmpty(s)) {
            return def;
        }
        try {
            int i = Integer.parseInt(s);
            return i;
        } catch (NumberFormatException e) {
            if(DEBUG) Log.w(TAG, "parse int exception:" + e);
            return def;
        }
    }

    public static int parseInt(String s) {
        return parseInt(s, 0);
    }

    public static long parseLong(String s, long def) {
        if (isEmpty(s)) {
            return def;
        }
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
            if(DEBUG) Log.w(TAG, "parse long exception:" + e);
            return def;
        }
    }

    public static float parseFloat(String s, float def) {
        if (isEmpty(s)) {
            return def;
        }
        try {
            float i = Float.parseFloat(s);
            return i;
        } catch (NumberFormatException e) {
            if(DEBUG) Log.w(TAG, "parse float exception:" + e);
            return def;
        }
    }
}
