package com.iccgame.sdk.common;

import android.annotation.SuppressLint;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ICC_Log {

    /**
     * 运行模式
     */
    public static boolean DEBUGGABLE = false;

    /**
     * 输出前缀
     */
    public static String TAG = "ICCGAME SDK";

    /**
     * 保存地址
     */
    public static String PATH = "";

    /**
     * 仅提供给开发者看的调试信息
     *
     * @param message
     */
    @SuppressLint({"LongLogTag"})
    public static void debug(String message) {
        if (message == null) {
            message = "";
        }
        if (DEBUGGABLE) {
            Log.d(TAG, message);
        }
        // 写入文件
        write("D", message);
    }

    /**
     * 常规信息
     *
     * @param message
     */
    @SuppressLint({"LongLogTag"})
    public static void info(String message) {
        if (message == null) {
            message = "";
        }
        Log.i(TAG, message);
        // 写入文件
        write("I", message);
    }

    /**
     * 警告信息
     *
     * @param message
     */
    @SuppressLint({"LongLogTag"})
    public static void warn(String message) {
        if (message == null) {
            message = "";
        }
        Log.w(TAG, message);
        // 写入文件
        write("W", message);
    }

    /**
     * 错误信息
     *
     * @param message
     */
    @SuppressLint({"LongLogTag"})
    public static void error(String message) {
        if (message == null) {
            message = "";
        }
        Log.e(TAG, message);
        // 写入文件
        write("E", message);
    }

    /**
     * 写入文件
     *
     * @param message
     */
    protected static boolean write(String type, String message) {
        if (PATH == null || PATH.length()<1) {
            return false;
        }
        File path = new File(PATH);
        try {
            // 调试创建
            if (DEBUGGABLE && path.exists() == false) {
                File parent = path.getParentFile();
                if (parent.exists() == false) {
                    parent.mkdirs();
                }
                path.createNewFile();
            }
            // 禁用功能
            if (path.exists() == false) {
                return false;
            }
            // 记录日志
            String line = String.format("%s\t%s\t%s\r\n", (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()), type, message);
            FileOutputStream outputStream = new FileOutputStream(path, true);
            outputStream.write(line.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    // End Class
}