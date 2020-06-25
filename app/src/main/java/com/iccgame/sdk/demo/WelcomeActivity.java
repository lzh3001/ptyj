package com.iccgame.sdk.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;

/**
 * 这是一个欢迎闪屏, 完全可以忽略
 *
 * @deprecated
 */
public class WelcomeActivity extends Activity {

    /**
     * 界面代码
     */
    protected static String VIEW_DATA = "<!DOCTYPE html><html><head><meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"><style>html,body{width:100%;height:100%}body{color:#00b0ff;font-family:'Arial';margin:0}h1{font-size:96px;font-weight:normal;line-height:100%;margin:0;text-align:center}h3{font-size:32px;font-weight:normal;margin:16px 0 0 0;text-align:center}h5{bottom:0;font-size:16px;font-weight:normal;height:48px;line-height:100%;margin:0;position:absolute;text-align:center;width:100%}.stage{height:38%;position:relative}.main{bottom:-72px;position:absolute;width:100%}</style></head><body><div class=\"stage\"><div class=\"main\"><h1>LOGO</h1><h3>Welcome</h3></div></div><h5><i>This is just a demo.</i></h5></body></html>";

    /**
     * 初始欢迎屏幕
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 创建内容
        WebView stage = new WebView(this.getApplicationContext());
        stage.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        stage.loadDataWithBaseURL(null, VIEW_DATA, "text/html", "utf-8", null);
        // 初始显示
        setContentView(stage);
        /**
         * 定时关闭
         */
        new Thread() {
            @Override
            public void run() {
                //　延迟等待
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 引导游戏
                setResult(Activity.RESULT_OK);
                finish();
            }
        }.start();
        // 开始显示
        super.onCreate(savedInstanceState);
    }

    /**
     * 监听按键退出
     *
     * @param keyCode
     * @param event
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }


    // End Class
}
