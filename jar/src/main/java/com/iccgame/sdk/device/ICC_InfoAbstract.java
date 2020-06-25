package com.iccgame.sdk.device;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by Administrator on 2015/7/30.
 */
abstract class ICC_InfoAbstract {

    /**
     * 上下文
     */
    public final Context context;

    /**
     * 构造函数
     *
     * @param context
     */
    public ICC_InfoAbstract(Context context) {
        this.context = context;
    }

    /**
     * 检查是否允许访问
     *
     * @param permission
     * @return
     */
    protected boolean checkPermission(String permission) {
        boolean value = false;
        try {
            // 获得当先包名
            String packageName = context.getPackageName();
            // 获得管理器对象
            PackageManager packageManager = context.getPackageManager();
            // 检测权限
            value = packageManager.checkPermission(permission, packageName) == PackageManager.PERMISSION_GRANTED;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }


    // End Class
}
