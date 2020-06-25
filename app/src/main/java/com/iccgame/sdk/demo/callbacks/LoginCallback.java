package com.iccgame.sdk.demo.callbacks;

import android.app.Activity;
import android.widget.Toast;

import com.iccgame.sdk.ICC_Callback;
import com.iccgame.sdk.demo.MainActivity;
import com.iccgame.sdk.demo.models.SDK_LoginModel;
import com.iccgame.sdk.demo.simulators.GameServerLoginSimulator;

public class LoginCallback implements ICC_Callback {

    /**
     * 上下文指针
     */
    public Object context = null;

    /**
     * 构造函数
     *
     * @param context
     */
    public LoginCallback(Object context) {
        this.context = context;
    }

    /**
     * 回调处理
     *
     * @param resultJSON
     */
    public void result(final String resultJSON) {
        // 转换数据
        SDK_LoginModel result = SDK_LoginModel.factory(resultJSON);
        // 当登录成功后，进入主界面
        if (result.getCode() == 0) {
            // 将用户信息保存到内存
            // code...
            // 验证请求真伪，获得账号信息。
            ((MainActivity) this.context).tmpUserInfo = GameServerLoginSimulator.verify(result.sdk_token).getUserInfo();
            // 演示代码结束
        } else if (result.getCode() == -3102) {
            // 用户取消登录时，直接退出游戏
            // 清理游戏相关资源
            // code...
            // 退出应用
            ((Activity) this.context).finish();
            System.exit(0);
        } else {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText((Activity) context, "登录失败。" + resultJSON, Toast.LENGTH_LONG).show();
                }
            });
        }
        // 释放资源
        this.context = null;
        // End Method
    }

    // End Class
}
