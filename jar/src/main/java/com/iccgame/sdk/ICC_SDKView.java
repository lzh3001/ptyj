package com.iccgame.sdk;

import android.app.Activity;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Toast;

import com.iccgame.sdk.common.ICC_Config;
import com.iccgame.sdk.common.ICC_Log;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/9/21.
 */
abstract class ICC_SDKView extends ICC_SDKBase {

    /**
     * 界面句柄
     */
    protected ICC_WebView _htmlView;

    /**
     * HTML5 是否处于工作状态
     */
    protected boolean _active = false;

    /**
     * 等待执行的 JAVASCRIPT 队列
     */
    protected ArrayList<String> _evalQueus = new ArrayList<String>();

    /**
     * 构造函数
     */
    protected ICC_SDKView(Activity activity) {
        // 继承父类
        super(activity);
    }

    /**
     * 首次初始并获得实例
     *
     * @param activity
     * @return
     */
    public static synchronized ICC_SDK getInstance(Activity activity) {
        // 初始对象
        ICC_SDK _instance = ICC_SDKBase.getInstance(activity);
        // 初始视图
        _instance._htmlView = new ICC_WebView(activity.getApplicationContext());
        // 返回结果
        return _instance;
    }

    /**
     * 是否活跃
     *
     * @return
     */
    public boolean isActive() {
        return this._active;
    }

    /**
     * 设置工作状态
     *
     * @param value
     */
    synchronized void setActive(boolean value) {
        this._active = value;
        if (value) {
            this.evalJavascript();
        } else {
            this.clearJavascript();
        }
        ICC_Log.info(String.format("ICC_SDKView.isActive() changed(%s)", value));
    }

    /**
     * 获得界面视图
     * 这是一个包内方法，不要添加public修饰符。
     *
     * @return
     */
    public synchronized ICC_WebView getView() {
        return this._htmlView;
    }

    /**
     * 在 HTML5 内执行 Javascript 代码。
     * 这是一个包内方法，不要添加public修饰符。
     *
     * @param script
     */
    synchronized void evalJavascript(final String script) {
        ICC_Log.debug(String.format("ICC_SDKView.evalJavascript(%s)", script));
        // 请求存储队列
        this._evalQueus.add(script);
        // 执行代码
        this.evalJavascript();
    }

    /**
     * 清除执行队列
     */
    synchronized void clearJavascript() {
        ICC_Log.debug("ICC_SDKView.clearJavascript()");
        this._evalQueus.clear();
    }

    /**
     * 执行掉队列中的脚本
     */
    synchronized void evalJavascript() {
        if (this.isActive() == false) {
            ICC_Log.warn("ICC_SDKView.isActive() equal to false");
            if (ICC_Config.SDK_TEXT_STARTING != null && ICC_Config.SDK_TEXT_STARTING.length() > 0) {
                Toast.makeText(
                        this.getApplicationContext(), ICC_Config.SDK_TEXT_STARTING, Toast.LENGTH_SHORT
                ).show();
            }
            return;
        }
        ICC_Log.debug(String.format("ICC_SDKView.evalJavascript(), Queus:%d", this._evalQueus.size()));
        while (this._evalQueus.isEmpty() == false) {
            this.getView().evalJavascript(this._evalQueus.remove(0));
        }
    }


    /**
     * 绑定内容到舞台
     *
     * @param stage
     */
    public synchronized boolean bind(ViewGroup stage) {
        ICC_Log.debug("ICC_SDKView.bind(ViewGroup)");
        ViewParent parent = this.getView().getParent();
        if (parent != null) {
            ICC_Log.warn("ICC_SDKView.getView().getParent() not equal to null");
            return false;
        }
        this.getView().setBackgroundColor(0);
        this.getView().setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        stage.addView(this.getView());
        return true;
    }

    /**
     * 解除内容绑定
     */
    public synchronized boolean unbind(ViewGroup stage) {
        ICC_Log.debug("ICC_SDKView.unbind(ViewGroup)");
        ViewGroup parent = (ViewGroup) this.getView().getParent();
        if (parent == null) {
            ICC_Log.warn("ICC_SDKView.getView().getParent() equal to null");
            return false;
        }
        if (stage.equals(parent) == false) {
            ICC_Log.warn("ICC_SDKView.getView().getParent() not equal to stage");
            return false;
        }
        parent.removeView(this.getView());
        return true;
    }

    /**
     * 此类被回收之前先进行HTML5清理
     */
    public synchronized void destroy() {
        ICC_Log.debug("ICC_SDKView.destroy()");
        // 释放内容
        this.getView().loadUrl("about:blank");
        this.setActive(false);
        // End Method
    }

    // End Class
}
