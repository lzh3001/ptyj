package com.iccgame.sdk;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Environment;

import com.iccgame.sdk.common.ICC_Config;
import com.iccgame.sdk.common.ICC_Log;

/**
 * 这是一个包内对象，不要添加public修饰符。
 */
abstract class ICC_SDKBase {

    /**
     * 单件句柄
     */
    protected static ICC_SDK _instance;

    /**
     * 进程句柄
     */
    protected Activity _gameActivity;

    /**
     * 应用上下文
     */
    protected final Context _applicationContext;

    /**
     * 构造函数
     */
    ICC_SDKBase(Activity activity) {
        // 修改级别
        ICC_Log.DEBUGGABLE = (0 != (activity.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE));
        ICC_Log.PATH = activity.getFilesDir() + ICC_Config.SDK_FOLDER + "/debug.log";
        // 绑定进程
        this._gameActivity = activity;
        this._applicationContext = activity.getApplicationContext();
    }

    /**
     * 获得实例
     *
     * @return ICC_SDK
     */
    public static synchronized ICC_SDK getInstance() {
        if (_instance == null) {
            throw new Error("First call must use ICC_SDK.getInstance(Activity activity).");
        }
        return _instance;
    }

    /**
     * 首次初始并获得实例
     *
     * @param activity
     * @return
     */
    public static synchronized ICC_SDK getInstance(Activity activity) {
        // 初始对象
        if (_instance == null) {
            ICC_Log.info("SDK Initialization");
            _instance = new ICC_SDK(activity);
        }
        // 返回结果
        return getInstance();
    }

    /**
     * 获得主进程句柄
     *
     * @return
     */
    public Activity getGameActivity() {
        return this._gameActivity;
    }

    /**
     * 获得应用上下文
     *
     * @return
     */
    public Context getApplicationContext() {
        return this._applicationContext;
    }

    // End Class
}
