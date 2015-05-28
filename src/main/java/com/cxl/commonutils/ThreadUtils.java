package com.cxl.commonutils;

import android.util.Log;

/**
 * Created by congxiaoliang on 15/5/28.
 */
public class ThreadUtils {
    private static final String TAG = "ThreadUtils";
    private static boolean DEBUG = ConfigFeature.DEBUG;

    public static void waitThread(Thread subThread, long millisTimeOut) {
        try {
            if (null == subThread) {
                return;
            }
            subThread.join(millisTimeOut);
        } catch (InterruptedException e) {
            if (DEBUG) Log.w(TAG, "waitThread exception:" + e);
        }
    }
}
