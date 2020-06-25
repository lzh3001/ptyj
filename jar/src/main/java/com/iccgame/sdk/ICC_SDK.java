package com.iccgame.sdk;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import com.iccgame.sdk.common.ICC_Log;
import com.iccgame.sdk.common.ICC_ScriptBuilder;

/**
 * Created by ZLei on 2015/6/30.
 */
public class ICC_SDK extends ICC_SDKCallbacks {

    /**
     * 所需权限
     */
    public static final String[] usePermissions = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * 构造函数
     */
    protected ICC_SDK(Activity activity) {
        // 继承父类
        super(activity);
        // 检查权限
        this.checkPermissions(activity);
    }

    /**
     * 检查权限
     *
     * @param context
     */
    protected void checkPermissions(Activity context) {
        try {
            // 检测权限
            boolean errors = false;
            for (String permission : usePermissions) {
                // 早起版本权限开放
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    if (permission == Manifest.permission.READ_EXTERNAL_STORAGE) {
                        continue;
                    }
                }
                // 检查权限
                if (context.checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
                    continue;
                }
                ICC_Log.warn(String.format("Permission denied, add <uses-permission android:name=\"%s\"/> in AndroidManifest.xml", permission));
                errors = true;
            }
            if (errors) {
                throw new Error("Permission denied.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 注册账号/试玩转正
     *
     * @param callback
     */
    public void register(ICC_Callback callback) {
        ICC_Log.info("ICC_SDK.register(ICC_Callback)");
        // 唤醒HTML5
        this.evalJavascript(
                String.format("window.ICCGAME_PASSPORT.register(%s);", this.registerCallback(callback))
        );
    }

    /**
     * 登录
     *
     * @param callback
     */
    public void login(ICC_Callback callback) {
        ICC_Log.info("ICC_SDK.login(ICC_Callback)");
        // 唤醒HTML5
        this.evalJavascript(
                String.format("window.ICCGAME_PASSPORT.login(%s);", this.registerCallback(callback))
        );
    }

    /**
     * 支付
     *
     * @param tradeInfo
     * @param callback
     */
    public void pay(String tradeInfo, ICC_Callback callback) {
        ICC_Log.info("ICC_SDK.pay(String, ICC_Callback)");
        // 唤醒HTML5
        this.evalJavascript(
                String.format("window.ICCGAME_PASSPORT.pay(\"%s\", %s);", tradeInfo, this.registerCallback(callback))
        );
    }

    /**
     * 账号中心
     *
     * @param callback
     */
    public void center(ICC_Callback callback) {
        ICC_Log.info("ICC_SDK.center(ICC_Callback)");
        // 唤醒HTML5
        this.evalJavascript(
                String.format("window.ICCGAME_PASSPORT.center(%s);", this.registerCallback(callback))
        );
    }

    /**
     * 切换账号
     *
     * @param callback
     */
    public void loginSwitch(ICC_Callback callback) {
        ICC_Log.info("ICC_SDK.loginSwitch(ICC_Callback)");
        // 唤醒HTML5
        this.evalJavascript(
                String.format("window.ICCGAME_PASSPORT.login_switch(%s);", this.registerCallback(callback))
        );
    }

    /**
     * 注销登录
     *
     * @param callback
     */
    public void logout(ICC_Callback callback) {
        ICC_Log.info("ICC_SDK.logout(ICC_Callback)");
        // 唤醒HTML5
        this.evalJavascript(
                String.format("window.ICCGAME_PASSPORT.logout(%s);", this.registerCallback(callback))
        );
    }

    /**
     * 退出游戏
     *
     * @param callback
     */
    public void exit(ICC_Callback callback) {
        ICC_Log.info("ICC_SDK.exit(ICC_Callback)");
        // 唤醒HTML5
        this.evalJavascript(
                String.format("window.ICCGAME_PASSPORT.exit(%s);", this.registerCallback(callback))
        );
    }

    /**
     * 设置浮标状态
     *
     * @param enabled
     */
    public void setAssistiveTouchState(boolean enabled) {
        ICC_Log.info(String.format("ICC_SDK.setAssistiveTouchState(%s)", enabled ? "true" : "false"));
        this.evalJavascript(
                ICC_ScriptBuilder.dispatchEvent("assistive_touch_state", enabled ? "true" : "false")
        );
    }

    // End Class
}
