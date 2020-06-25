package com.iccgame.sdk;

import android.os.Handler;
import android.os.Looper;

import com.iccgame.sdk.common.ICC_Log;

public class JniHelper {

    /**
     * 功能列表
     */
    public final static int METHOD_REGISTER = 1;
    public final static int METHOD_LOGIN = 2;
    public final static int METHOD_PAY = 4;
    public final static int METHOD_CENTER = 8;
    public final static int METHOD_LOGOUT = 16;

    /**
     * 结果回调
     *
     * @param resultJSON
     */
    public native static void result(int handle, String resultJSON);

    /**
     * 测试代码
     * @param method
     * @param paramsJSON
     */
    public native static void tester(int method, String paramsJSON);

    /**
     * 处理方法调用消息
     *
     * @param method
     * @param params
     */
    public static void callSdkMethod(final int method, final int handle, final String params) {
        ICC_Log.debug(String.format("ICC_SDKJni.callSdkMethod(method:%d, handle:%d, params:%s)", method, handle, params));
        // 提交消息
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                dispatch(method, handle, params);
            }
        });
    }

    /**
     * 进行实际调用
     *
     * @param method
     * @param handle
     * @param params
     */
    static void dispatch(int method, int handle, String params) {
        ICC_Log.debug("ICC_SDKJni.dispatch(int, int, String)");
        JniCallback callback = new JniCallback(handle);
        if (method == METHOD_REGISTER) {
            ICC_Log.info("JNI Interface call ICC_SDK.register(JniCallback)");
            ICC_SDK.getInstance().register(callback);
        } else if (method == METHOD_LOGIN) {
            ICC_Log.info("JNI Interface call ICC_SDK.login(JniCallback)");
            ICC_SDK.getInstance().login(callback);
        } else if (method == METHOD_PAY) {
            ICC_Log.info("JNI Interface call ICC_SDK.pay(String, JniCallback)");
            ICC_SDK.getInstance().pay(params, callback);
        } else if (method == METHOD_CENTER) {
            ICC_Log.info("JNI Interface call ICC_SDK.center(JniCallback)");
            ICC_SDK.getInstance().center(callback);
        } else if (method == METHOD_LOGOUT) {
            ICC_Log.info("JNI Interface call ICC_SDK.logout(JniCallback)");
            ICC_SDK.getInstance().logout(callback);
        }
    }

    /**
     * Jni 回调传递
     */
    public static class JniCallback implements ICC_Callback {

        /**
         * 回调句柄
         */
        public final int handle;

        /**
         * 构造函数
         *
         * @param handle
         */
        public JniCallback(int handle) {
            this.handle = handle;
        }

        /**
         * 结果处理
         *
         * @param resultJSON
         */
        public void result(String resultJSON) {
            ICC_Log.debug(String.format("JniHelper.JniCallback.result(%d, %s)", this.handle, resultJSON));
            try {
                JniHelper.result(this.handle, resultJSON);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // End Sub Class
    }
    // End Class
}
