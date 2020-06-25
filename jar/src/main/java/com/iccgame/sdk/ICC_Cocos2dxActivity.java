package com.iccgame.sdk;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.iccgame.sdk.common.ICC_Log;

import org.cocos2dx.lib.Cocos2dxActivity;

public class ICC_Cocos2dxActivity extends Cocos2dxActivity {

    /**
     * 当应用被创建调用
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 继承父类
        super.onCreate(savedInstanceState);
        // 初始 SDK
        ICC_SDK.getInstance(this);
    }

    /**
     * 当应用被销毁调用
     */
    @Override
    protected void onDestroy() {
        // 析构 SDK
        ICC_SDK.getInstance().destroy();
        // 继承父类
        super.onDestroy();
    }

    /**
     * Cocos2d 调用 SDK 方法
     *
     * @param method
     * @param handle
     * @param params
     */
    public static void callSdkMethod(final int method, final int handle, final String params) {
        ICC_Log.debug(String.format("ICC_Cocos2dxActivity.callSdkMethod(method:%d, handle:%d, params:%s)", method, handle, params));
        try {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    JniHelper.dispatch(method, handle, params);
                }
            });
        } catch (Exception e) {
            ICC_Log.error(e.getMessage());
        }
    }

    // End Class
}
