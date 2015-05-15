package com.cxl.commonutils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * Created by congxiaoliang on 15/5/13.
 */
public class ActivityUtils {
    private static final String TAG = "ActivityUtils";
    private static boolean DEBUG = BuildConfig.DEBUG;

    private static ActivityUtils sActivityUtils = null;
    private static Context sContext = null;

    public static ActivityUtils getInstance(Context ctx) {
        if (null == sActivityUtils) {
            sActivityUtils = new ActivityUtils();
        }
        sContext = ctx;
        return sActivityUtils;
    }

    /**
     * 获取版本号
     * @param pkgName
     * @return 当前应用的版本号, 格式：versionName | versionCode
     */
    public String getAppVersion(String pkgName) {
        String versionInfo = "";
        try {
            PackageManager manager = sContext.getPackageManager();
            PackageInfo info = manager.getPackageInfo(pkgName, 0);
            if (null == info) {
                return versionInfo;
            }
            String versionName = info.versionName;
            int versionCode = info.versionCode;
            versionInfo = versionName + " | " + versionCode;
            if (DEBUG) Log.d(TAG, "@@@versionName:" + versionName + ", versionCode:" + versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            // not found
            if (DEBUG) Log.e(TAG, "@@@app is not found, exception:" + e);
            versionInfo = "";
        } catch (Exception e) {
            // not found
            if (DEBUG) Log.e(TAG, "@@@app is not found, exception:" + e);
            versionInfo = "";
        }
        return versionInfo;
    }

    /**
     * 判断Activity是否存在。---目前还未使用过。
     * @param pkgName
     * @param activityName
     * @return 当前应用的版本号, 格式：versionName | versionCode
     */
    public boolean isActivityExsist(String pkgName, String activityName) {
        Intent intent = new Intent();
        intent.setClassName(pkgName, activityName);
        PackageManager manager = sContext.getPackageManager();
        ComponentName name = intent.resolveActivity(manager);
        if (null == name) {
            // 说明系统中不存在这个activity
            return false;
        }
        if (DEBUG) Log.d(TAG, "@@@ComponentName:" + name);
        return true;
    }

}
