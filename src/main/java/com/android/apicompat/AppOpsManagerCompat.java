package com.android.apicompat;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import com.cxl.commonutils.BuildConfig;
import com.cxl.commonutils.ConfigFeature;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * AppOpsManager权限检查工具类
 */
public class AppOpsManagerCompat {
    private static final boolean DEBUG = ConfigFeature.DEBUG;
    private static final String TAG = "AppOpsManagerCompat";

    /**
     * OP_* 权限类型值与AppOpsManager定义相同
     */
    public static final int OP_READ_CONTACTS = 4;
    public static final int OP_READ_CALL_LOG = 6;
    public static final int OP_POST_NOTIFICATION = 11;
    public static final int OP_READ_SMS = 14;
    public static final int OP_SYSTEM_ALERT_WINDOW = 24;

    private static int sCheckSupportOps = -1;
    private static boolean sHaveInitOpsMethod = false;

    private static Method sCheckOpMethod = null;
    private static AppOpsManager sAppOpsManager = null;


    /**
     * 初始化反射接口
     * @param ctx:context
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static void initOpsMethod(Context ctx) {
        try {
            sAppOpsManager = (AppOpsManager) ctx.getSystemService(Context.APP_OPS_SERVICE);
            if (null != sAppOpsManager) {
                sCheckOpMethod = sAppOpsManager.getClass().getMethod("checkOpNoThrow",
                        new Class[]{int.class, int.class, String.class});
            }
        } catch (NoSuchMethodException e) {
            if (DEBUG) Log.w(TAG, "Unexpected excetion: ", e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 反射调用AppOpsManager接口检查权限
     * @param ctx:context
     * @param pkgName:App包名
     * @param op：OP权限类型
     * @return -1/2: 失败；0：权限允许； 1：权限禁止
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static int checkOpsPermission(Context ctx, String pkgName, int op) {
        if (!isSupportOps()) {
            return -1;
        }
        if (!sHaveInitOpsMethod) {
            initOpsMethod(ctx);
        }
        if (null == sCheckOpMethod || null == sAppOpsManager) {
            return -1;
        }
        try {
            ApplicationInfo applicationInfo = ctx.getPackageManager().getApplicationInfo(
                                                pkgName, 0);
            int uid = applicationInfo.uid;
            int nResult = (Integer) sCheckOpMethod.invoke(sAppOpsManager, op, uid, pkgName);
            return nResult;
        } catch (PackageManager.NameNotFoundException e) {
            if (DEBUG) Log.w(TAG, "Unexpected excetion: ", e);
        } catch (IllegalAccessException e) {
            if (DEBUG) Log.w(TAG, "Unexpected excetion: ", e);
        } catch (InvocationTargetException e) {
            if (DEBUG) Log.w(TAG, "Unexpected excetion: ", e);
        } catch (Exception e) {
            if (DEBUG) Log.w(TAG, "Unexpected excetion: ", e);
        }
        return -1;
    }

    /**
     * 检查是否支持AppOpsManager检查权限
     * 条件需要满足
     * 1. SDK >= KITKAT
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static boolean isSupportOps() {
        if (-1 == sCheckSupportOps) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                sCheckSupportOps = 1;
            } else {
                sCheckSupportOps = 0;
            }
        }
        return (1 == sCheckSupportOps);
    }
}
