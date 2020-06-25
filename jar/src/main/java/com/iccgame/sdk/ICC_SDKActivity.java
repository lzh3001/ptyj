package com.iccgame.sdk;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;

import com.iccgame.sdk.common.ICC_Log;

import java.util.GregorianCalendar;

/**
 * Created by Administrator on 2015/9/21.
 */
abstract class ICC_SDKActivity extends ICC_SDKView {
    /**
     * 当前活动窗口
     */
    protected ICC_Activity _htmlActivity = null;

    /**
     * 构造函数
     */
    protected ICC_SDKActivity(Activity activity) {
        // 继承父类
        super(activity);
    }

    /**
     * 获得前置进程句柄
     *
     * @return
     */
    public Activity getHtmlActivity() {
        return this._htmlActivity;
    }

    /**
     * 内部绑定一个Activity对象用于销毁。
     *
     * @param activity
     */
    synchronized void registerChildActivity(ICC_Activity activity) {
        ICC_Log.info("ICC_SDKActivity.registerChildActivity(ICC_Activity)");
        this._htmlActivity = activity;
        //mWindowManager.addView(mDesktopLayout, mLayout);
    }

    /**
     * 挂起内容
     * 这是一个包内方法，不要添加public修饰符。
     */
    synchronized boolean createHtmlActivity() {
        ICC_Log.info("ICC_SDKActivity.createHtmlActivity()");
        if (this.getView().getParent() != null) {
            ICC_Log.warn("ICC_SDKActivity.getView().getParent() not equal to null");
            return false;
        }
        if (this.getHtmlActivity() != null) {
            ICC_Log.warn("ICC_SDKActivity.getHtmlActivity() not equal to null");
            return false;
        }
//        new Handler(Looper.getMainLooper()).post(new Runnable() {
//            @Override
//            public void run() {
        Intent intent = new Intent(this.getApplicationContext(), ICC_Activity.class);
        this.getGameActivity().startActivity(intent);
//            }
//        });
        //done
        return true;
    }

    /**
     * 解除挂起
     * 这是一个包内方法，不要添加public修饰符。
     */
    synchronized boolean finishHtmlActivity() {
        ICC_Log.info("ICC_SDKActivity.finishHtmlActivity()");
        if (this._htmlActivity == null) {
            ICC_Log.warn("ICC_SDKActivity.getHtmlActivity() equal to null");
            return false;
        }
        // 释放窗口
//        new Handler(Looper.getMainLooper()).post(new Runnable() {
//            @Override
//            public void run() {
        _htmlActivity._stage.removeAllViews();
        _htmlActivity.finish();
        _htmlActivity = null;
//            }
//        });
        return true;
    }

    /**
     * 此类被回收之前先进行HTML5清理
     */
    public synchronized void destroy() {
        ICC_Log.info("ICC_SDKActivity.destroy()");
        // 释放窗口
        this.finishHtmlActivity();
        // 继承父类
        super.destroy();
    }
    // End Class
}
