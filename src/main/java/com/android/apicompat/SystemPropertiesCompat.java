/*
 * Copyright (C) 2013 Tapas Mobile Ltd.  All Rights Reserved.
 */

package com.android.apicompat;

import android.util.Log;

import com.cxl.commonutils.ConfigFeature;
import com.cxl.commonutils.StringUtils;

import java.lang.reflect.Method;

public class SystemPropertiesCompat {
    private static final boolean DEBUG = ConfigFeature.DEBUG;
    private static final String TAG = "SystemBuildPropCompat";

    private static final String CLASSNAME_SYSTEMPROPERTIES = "android.os.SystemProperties";
    private static Method sGetStringSystemPorpertes;

    static {
        try {
            Class<?> clazz = Class.forName(CLASSNAME_SYSTEMPROPERTIES, false,
                    Thread.currentThread().getContextClassLoader());
            sGetStringSystemPorpertes = clazz.getMethod("get", String.class, String.class);
        } catch (Exception e) {
            if (DEBUG) e.printStackTrace();
            sGetStringSystemPorpertes = null;
        }
    }

    /**
     * Get the value for the given key, and return as an integer.
     * @return if the key isn't found, return def if it isn't null, or an empty string otherwise
     * @param key the key to lookup
     */
    public static int getInt(String key, int def) {
        String result = getString(key, String.valueOf(def));
        return StringUtils.parseInt(result, def);
    }

    /**
     * Get the value for the given key, and return as string.
     * @return if the key isn't found, return def if it isn't null, or an empty string otherwise
     * @param key the key to lookup
     */
    public static String getString(String key, String def) {
        String result = def;
        if (sGetStringSystemPorpertes != null) {
            try {
                Method method = sGetStringSystemPorpertes;
                Object[] arrayOfObject = new Object[] {
                        key, def
                };
                result = (String) method.invoke(null, arrayOfObject);
            } catch (Exception e) {
                if (DEBUG) Log.w(TAG, "getString exception: " + e);
            }
        }
        return result;
    }
}
