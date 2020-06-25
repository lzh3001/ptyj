package com.iccgame.sdk.demo.callbacks;


import com.iccgame.sdk.ICC_Callback;

public class RegisterCallback implements ICC_Callback {

    /**
     * 上下文指针
     */
    public Object context = null;

    /**
     * 构造函数
     *
     * @param context
     */
    public RegisterCallback(Object context) {
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
        // 释放资源
        this.context = null;
    }

}
