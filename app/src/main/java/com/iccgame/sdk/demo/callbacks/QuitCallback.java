package com.iccgame.sdk.demo.callbacks;

import com.iccgame.sdk.ICC_Callback;
import com.iccgame.sdk.demo.MainActivity;
import com.iccgame.sdk.demo.models.SDK_CommonModel;

public class QuitCallback implements ICC_Callback {

    /**
     * 上下文指针
     */
    public Object context = null;

    /**
     * 构造函数
     *
     * @param context
     */
    public QuitCallback(Object context) {
        this.context = context;
    }

    /**
     * 回调处理
     *
     * @param resultJSON
     */
    public void result(String resultJSON) {
        /**
         * 此函数内容，请游戏客户端填写自己的代码
         */
        final SDK_CommonModel result = SDK_CommonModel.factory(resultJSON);
        if (result.getCode() == 0) {
            ((MainActivity) this.context).finish();
            System.exit(0);
        }
        // 释放资源
        this.context = null;
    }

}
