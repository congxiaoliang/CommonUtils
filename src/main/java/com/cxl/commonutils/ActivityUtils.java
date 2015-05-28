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



    private static ActivityUtils sActivityUtils = null;
    private static Context sContext = null;

    public static synchronized ActivityUtils getInstance(Context ctx) {
        if (null == sActivityUtils) {
            sActivityUtils = new ActivityUtils();
        }
        sContext = ctx;
        return sActivityUtils;
    }

    /**
     * 获取版本号
     * @param pkgName
     * @return 当前应用的版本号, null或格式：versionName | versionCode.
     */
    public String getAppVersion(String pkgName) {
        String versionInfo = null;
        if (null == pkgName) {
            return versionInfo;
        }
        try {
            PackageManager manager = sContext.getPackageManager();
            PackageInfo info = manager.getPackageInfo(pkgName, 0);
            if (null == info) {
                return versionInfo;
            }
            String versionName = info.versionName;
            int versionCode = info.versionCode;
            versionInfo = versionName + " | " + versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // not found
            if (DEBUG) Log.e(TAG, "@@@getAppVersion-NameNotFoundException:" + e);
        } catch (Exception e) {
            // not found
            if (DEBUG) Log.e(TAG, "@@@getAppVersion-Exception:" + e);
        }
        return versionInfo;
    }

    /**
     * 通过包名获取主Activity的Intent，主要用于获取主Activity名字
     * @param pkgName
     * @return Intent
     */
    public Intent getMainActivityIntent(String pkgName) {
//        PackageManager manager = sContext.getPackageManager();
//        Intent it = manager.getLaunchIntentForPackage(pkgName);
//        return it;

        return getLaunchIntentForPackage(pkgName);
    }

    /**
     * 通过包名获取主Activity的Intent，主要用于获取主Activity名字, 同Framework
     * PackageManager.getLaunchIntentForPackage()
     * @param packageName
     * @return Intent
     */
    private Intent getLaunchIntentForPackage(String packageName) {

        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageManager manager = sContext.getPackageManager();

        // First see if the package has an INFO activity; the existence of
        // such an activity is implied to be the desired front-door for the
        // overall package (such as if it has multiple launcher entries).
        Intent intentToResolve = new Intent(Intent.ACTION_MAIN);
        intentToResolve.addCategory(Intent.CATEGORY_INFO);
        intentToResolve.setPackage(packageName);
        List<ResolveInfo> ris = manager.queryIntentActivities(intentToResolve, 0);

        // Otherwise, try to find a main launcher activity.
        if (ris == null || ris.size() <= 0) {
            // reuse the intent instance
            intentToResolve.removeCategory(Intent.CATEGORY_INFO);
            intentToResolve.addCategory(Intent.CATEGORY_LAUNCHER);
            intentToResolve.setPackage(packageName);
            ris = manager.queryIntentActivities(intentToResolve, 0);
        }
        if (ris == null || ris.size() <= 0) {
            return null;
        }
        Intent intent = new Intent(intentToResolve);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClassName(ris.get(0).activityInfo.packageName,
                ris.get(0).activityInfo.name);
        return intent;
    }

    public void openInstalledDetail(String pkgName) {
        Intent intent = getAppDetailsIntent(pkgName);
        if (isActivityAvailable(intent)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            sContext.startActivity(intent);
        }
    }

    public Intent getAppDetailsIntent(String pkgName) {
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
    public int startMainActivity(String pkgName) {
        Intent it = getMainActivityIntent(pkgName);
        if (null == it) {
            return FLAG_DEFAULT;
        }
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return startActivity(it);
    }

    /**
     * 获取版本号
     * @param intent
     * @return FLAG_*
     */
    public int startActivity(Intent intent) {
        try {
            if (null == intent) {
                return FLAG_DEFAULT;
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            sContext.startActivity(intent);
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

    public boolean isActivityAvailable(Intent intent) {
        List<ResolveInfo> list = sContext.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list != null && list.size() > 0;
    }

    /**
     * 判断Activity是否存在。---未被调用用过。
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
        if (DEBUG) Log.d(TAG, "@@@isActivityExsist-ComponentName:" + name);
        return true;
    }

}
