package com.cxl.commonutils;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by congxiaoliang on 15/5/13.
 */
public class FileUtils {
    private static final String TAG = "FileUtils";
    private static boolean DEBUG = BuildConfig.DEBUG;


    /**
     * 写文件
     * @param filePath
     * @param fileName
     * @param msg
     * @return Success or fail
     */
    public static boolean write(String filePath, String fileName, String msg) {
        File dir = new File(filePath);
        if (!dir.exists()) {
            dir.mkdir();
        }
        File outFile = new File(dir, fileName);
        OutputStreamWriter outWriter = null;
        try {
            outWriter = new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8");
            outWriter.write(msg);
            outWriter.flush();
        } catch (IOException e) {
            if (DEBUG) Log.e(TAG, "write-exception, e:" + e);
            return false;
        } finally {
            try {
                outWriter.close();
            } catch (Exception e) {
                // ignore
                return false;
            }
        }
        return true;

    }
}
