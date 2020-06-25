package com.iccgame.sdk.demo.callbacks;


import android.app.Activity;
import android.widget.Toast;

import com.iccgame.sdk.ICC_Callback;
import com.iccgame.sdk.demo.models.SDK_PayModel;

public class PayCallback implements ICC_Callback {

    /**
     * 上下文指针
     */
    public Object context = null;

    /**
     * 构造函数
     *
     * @param context
     */
    public PayCallback(Object context) {
        this.context = context;
    }

    /**
     * 回调处理
     *
     * @param resultJSON
     */
    public void result(final String resultJSON) {
        final SDK_PayModel result = SDK_PayModel.factory(resultJSON);
        // 当支付成功后，回到游戏
        if (result.getCode() == 0) {
            // 支付成功，请游戏客户端填写自己的代码
            // code...
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText((Activity) context, result.getTradeNo(), Toast.LENGTH_LONG).show();
                }
            });

        } else if (result.getCode() == -3102) {
            // 用户取消，请游戏客户端填写自己的代码
            // code...
        } else {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText((Activity) context, "支付失败。" + resultJSON, Toast.LENGTH_LONG).show();
                }
            });
        }
        // 释放资源
        this.context = null;
        // End Method
    }

}
