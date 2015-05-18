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
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(outFile);
            outWriter = new OutputStreamWriter(fos, "UTF-8");
            outWriter.write(msg);
            outWriter.flush();
            return true;
        } catch (IOException e) {
            if (DEBUG) Log.e(TAG, "write-exception, e:" + e);
            return false;
        } finally {
            close(outWriter);
            close(fos);
        }
    }

    /**
     * 写文件
     * @param filePath
     * @param fileName
     * @return file's content, saved to StringBuffer
     */
    public static StringBuffer read(String filePath, String fileName){
        InputStreamReader isr = null;
        FileInputStream fis = null;
        BufferedReader bufferedReader = null;
        StringBuffer sb = null;
        try {
            String encoding = "UTF-8";
            File file = new File(filePath, fileName);

            if(file.isFile() && file.exists()) { //判断文件是否存在
                fis = new FileInputStream(file);
                isr = new InputStreamReader(fis, encoding);
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

        } catch (UnsupportedEncodingException e) {

        } catch (IOException e) {

        } catch (Exception e) {

        } finally {
            close(bufferedReader);
            close(isr);
            close(fis);
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
