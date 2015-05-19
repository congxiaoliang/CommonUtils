package com.cxl.commonutils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by congxiaoliang on 15/5/13.
 */
public class DumpUtils {
    private static final String TAG = "DumpUtils";
    private static boolean DEBUG = BuildConfig.DEBUG;

    /**
     * 执行adb or shell命令等---未被调用过。
     * ---dumpsys相关命令需要DUMP permission，权限只有系统应用才能申请，第三方app无法执行dumpsys命令
     * @param command
     * @param sb
     * @return Success or fail
     */
    private static boolean execCommand(String command, StringBuffer sb) {
        Process proc = null;
        try {
            Runtime runtime = Runtime.getRuntime();
            proc = runtime.exec(command);
            if (proc.waitFor() != 0) {
                if (DEBUG) Log.w(TAG, "exit value = " + proc.exitValue());
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    proc.getInputStream()));
            String line = null;

            sb.append("------Execute Cmd: ").append(command).append(" start------").append("\n");
            while ((line = in.readLine()) != null) {
                sb.append(line).append("\n");
            }
            sb.append("------Execute Cmd: ").append(command).append(" end------").append("\n");
            return true;
        } catch (IOException e) {
            if (DEBUG) Log.w(TAG, "execCommand exception:" + e);
            return false;
        } catch (InterruptedException e) {
            if (DEBUG) Log.w(TAG, "execCommand exception:" + e);
            return false;
        } finally{
            try {
                if (null != proc) proc.destroy();
            } catch (Exception e) {
                if (DEBUG) Log.w(TAG, "execCommand exception:" + e);
            }
        }
    }

}
