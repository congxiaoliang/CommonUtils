package com.cxl.commonutils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

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
            FileOutputStream fos = new FileOutputStream(outFile);
            outWriter = new OutputStreamWriter(fos, "UTF-8");
            outWriter.write(msg);
            outWriter.flush();
            return true;
        } catch (IOException e) {
            if (DEBUG) Log.e(TAG, "write-exception, e:" + e);
            return false;
        } finally {
            // outWriter close时会调用FileOutputStream的close。
            close(outWriter);
        }
    }

    /**
     * 写文件
     * @param filePath
     * @param fileName
     * @return file's content, saved to StringBuffer
     */
    public static StringBuffer read(String filePath, String fileName){
        BufferedReader bufferedReader = null;
        StringBuffer sb = null;
        try {
            String encoding = "UTF-8";
            File file = new File(filePath, fileName);

            if(file.isFile() && file.exists()) { //判断文件是否存在
                FileInputStream fis = new FileInputStream(file);
                InputStreamReader isr = new InputStreamReader(fis, encoding);
                bufferedReader = new BufferedReader(isr);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                    if (null == sb) {
                        sb = new StringBuffer();
                    }
                    sb.append(lineTxt);
                }
            } else {

            }
        } catch (FileNotFoundException e) {
            if (DEBUG) Log.e(TAG, "read-exception, e:" + e);
        } catch (UnsupportedEncodingException e) {
            if (DEBUG) Log.e(TAG, "read-exception, e:" + e);
        } catch (IOException e) {
            if (DEBUG) Log.e(TAG, "read-exception, e:" + e);
        } catch (Exception e) {
            if (DEBUG) Log.e(TAG, "read-exception, e:" + e);
        } finally {
            // bufferedReader close时会调InputStreamReader的close
            close(bufferedReader);
        }
        return sb;
    }

    /**
     * Close a {@link java.io.Closeable} object and ignore the exception.
     * @param target The target to close. Can be null.
     */
    public static void close(Closeable target) {
        try {
            if (target != null) {
                target.close();
            }
        } catch (IOException e) {
            if (DEBUG) Log.w(TAG, "Failed to close the target. ", e);
        }
    }
}
