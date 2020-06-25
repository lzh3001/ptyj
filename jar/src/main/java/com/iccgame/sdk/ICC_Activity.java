package com.iccgame.sdk;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.RelativeLayout;

import com.iccgame.sdk.common.ICC_Log;
import com.iccgame.sdk.common.ICC_ScriptBuilder;

//import android.renderscript.ScriptIntrinsicBlur;

/**
 * Created by ZLei on 2015/7/2.
 */
public class ICC_Activity extends Activity {

    /**
     * 根部内容
     */
    RelativeLayout _stage;

    /**
     * 窗口创建
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ICC_Log.info("ICC_Activity.onCreate(Bundle)");
        // 继承父类
        super.onCreate(savedInstanceState);
        // 初始内容
        this._stage = new RelativeLayout(this);
        this._stage.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        // 设置内容
        setContentView(this._stage);
    }

    /**
     * 析构创建
     */
    protected void onDestroy() {
        ICC_Log.info("ICC_Activity.onDestroy()");
        super.onDestroy();
    }

    /**
     * 当窗口显示
     */
    @Override
    protected void onStart() {
        ICC_Log.info("ICC_Activity.onStart()");
        // 用来返回跳转
        ICC_SDK.getInstance().registerChildActivity(this);
        // 绑定显示
        ICC_SDK.getInstance().bind(this._stage);
        // 继承父类
        super.onStart();
    }

    /**
     * 当窗口关闭
     */
    @Override
    protected void onStop() {
        ICC_Log.info("ICC_Activity.onStop()");
        // 绑定显示
        ICC_SDK.getInstance().unbind(this._stage);
        // 继承父类
        super.onStop();
    }

    /**
     * 将部分功能按键传递给HTML5处理
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //返回键
        ICC_SDK.getInstance().evalJavascript(ICC_ScriptBuilder.dispatchKeyEvent("keystart", keyCode));
        return true;
    }

    /**
     * 将部分功能按键传递给HTML5处理
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        //返回键
        ICC_SDK.getInstance().evalJavascript(ICC_ScriptBuilder.dispatchKeyEvent("keystop", keyCode));
        return true;
    }

    // End Class
}
