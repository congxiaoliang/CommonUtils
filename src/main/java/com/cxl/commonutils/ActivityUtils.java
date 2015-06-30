package com.cxl.commonutils;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import java.util.List;

/**
 * Created by congxiaoliang on 15/5/13.
 */
public class ActivityUtils {
    private static final String TAG = "ActivityUtils";
    private static boolean DEBUG = ConfigFeature.DEBUG;

    /**
     * Flag for startActivity's return
     */
    public static final int FLAG_EXCEPTION = -3;
    public static final int FLAG_EXCEPTION_SECURITY = -2;
    public static final int FLAG_EXCEPTION_NOTFOUND = -1;
    public static final int FLAG_DEFAULT = 0;
    public static final int FLAG_SUCCESS = 1;

    /**
     * 获取版本号
     * @param pkgName
     * @return Instance of PackageInfo or null
     */
    private static PackageInfo getPkgInfo(Context ctx, String pkgName) {
        try {
            PackageManager manager = ctx.getPackageManager();
            PackageInfo info = manager.getPackageInfo(pkgName, 0);
            return info;
        } catch (PackageManager.NameNotFoundException e) {
            if (DEBUG) Log.w(TAG, "getPkgInfo-NameNotFoundException:" + e);
        } catch (Exception e) {
            if (DEBUG) Log.w(TAG, "getPkgInfo-Exception:" + e);
        }
        return null;
    }

    /**
     * 获取版本号
     * @param pkgName
     * @return 当前应用的版本号, null或格式：versionName | versionCode.
     */
    public static String getAppVersion(Context ctx, String pkgName) {
        String versionInfo = null;
        if (null == pkgName) {
            return versionInfo;
        }
        PackageInfo info = getPkgInfo(ctx, pkgName);
        if (null == info) {
            return versionInfo;
        }
        String versionName = info.versionName;
        int versionCode = info.versionCode;
        versionInfo = versionName + " | " + versionCode;
        return versionInfo;
    }

    public static void openInstalledDetail(Context ctx, String pkgName) {
        Intent intent = getAppDetailsIntent(pkgName);
        if (isActivityAvailable(ctx, intent)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(intent);
        }
    }

    private static boolean isActivityAvailable(Context ctx, Intent intent) {
        List<ResolveInfo> list = ctx.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list != null && list.size() > 0;
    }

    private static Intent getAppDetailsIntent(String pkgName) {
        Intent intent;
        if (Build.VERSION.SDK_INT >= 9) {
            intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.fromParts("package", pkgName, null));
        } else {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings",
                    "com.android.settings.InstalledAppDetails");
            if (Build.VERSION.SDK_INT == 8) {
                intent.putExtra("pkg", pkgName);
            } else {  // SDK_VERSION == 7
                intent.putExtra("com.android.settings.ApplicationPkgName", pkgName);
            }
        }
        return intent;
    }

    /**
     * 通过包名启动主Activity
     * @param pkgName
     * @return FLAG_*
     */
    public static int startMainActivity(Context ctx, String pkgName) {
        PackageManager manager = ctx.getPackageManager();
        Intent it = manager.getLaunchIntentForPackage(pkgName);
        if (null == it) {
            return FLAG_DEFAULT;
        }
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return startActivity(ctx, it);
    }

    /**
     * 获取版本号
     * @param intent
     * @return FLAG_*
     */
    public static int startActivity(Context ctx, Intent intent) {
        try {
            if (null == intent) {
                return FLAG_DEFAULT;
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(intent);
            return FLAG_SUCCESS;
        } catch (ActivityNotFoundException e) {
            if (DEBUG) Log.w(TAG, "@@@startActivity e:" + e);
            return FLAG_EXCEPTION_NOTFOUND;
        } catch (SecurityException e) {
            if (DEBUG) Log.w(TAG, "@@@startActivity e:" + e);
            return FLAG_EXCEPTION_SECURITY;
        } catch (Exception e) {
            if (DEBUG) Log.w(TAG, "@@@startActivity e:" + e);
            return FLAG_EXCEPTION;
        }
    }

    /**
     * 判断Activity是否存在。---未被调用用过。
     * @param pkgName
     * @param activityName
     * @return 当前应用的版本号, 格式：versionName | versionCode
     */
    public static boolean isActivityExsist(Context ctx, String pkgName, String activityName) {
        Intent intent = new Intent();
        intent.setClassName(pkgName, activityName);
        PackageManager manager = ctx.getPackageManager();
        ComponentName name = intent.resolveActivity(manager);
        if (null == name) {
            // 说明系统中不存在这个activity
            return false;
        }
        if (DEBUG) Log.d(TAG, "@@@isActivityExsist-ComponentName:" + name);
        return true;
    }

}
