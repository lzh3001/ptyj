package com.iccgame.sdk.device;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import com.iccgame.sdk.common.ICC_Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/7/30.
 */
public class ICC_InfoOfDevice extends ICC_InfoAbstract {

    /**
     * 构造函数
     *
     * @param context
     */
    public ICC_InfoOfDevice(Context context) {
        super(context);
    }

    /**
     * 获得设备名称
     *
     * @return
     */
    public String getDevice() {
        return Build.MODEL;
    }

    /**
     * 获得安装的软件列表
     *
     * @return
     */
    public String getInstalledPackages() {
        ArrayList<String> value = new ArrayList<String>();
        try {
            // 获得管理器对象
            PackageManager packageManager = context.getPackageManager();
            // 遍历安装软件
            List<PackageInfo> packages = packageManager.getInstalledPackages(0);
            for (PackageInfo item : packages) {
                if ((item.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    value.add(item.applicationInfo.loadLabel(packageManager).toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return TextUtils.join("\n", value);
    }

    /**
     * 获得当前版本
     *
     * @return
     */
    public String getPackageVersion() {
        String value = "";
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            value = packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 获得当前包名
     *
     * @return
     */
    public String getPackageName() {
        return context.getPackageName();
    }

    /**
     * 获得手机序列号
     *
     * @return
     */
    public String getSerialNumber() {
        return Build.SERIAL;
    }


    /**
     * 获得扩展存储路径
     *
     * @return
     */
    public String getExternalStoragePath() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) == false) {
            ICC_Log.warn(Manifest.permission.READ_EXTERNAL_STORAGE + " permission denied.");
            return "";
        }
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            ICC_Log.warn(Manifest.permission.WRITE_EXTERNAL_STORAGE + " permission denied.");
        }
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    /**
     * 下载地址
     *
     * @return
     */
    public String getDownloadPath() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
    }

    /**
     * 图片地址
     *
     * @return
     */
    public String getPicturePath() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
    }

    // End Class
}
