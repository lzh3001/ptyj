package com.iccgame.sdk.demo.callbacks;


import com.iccgame.sdk.ICC_Callback;

public class CenterCallback implements ICC_Callback {

    /**
     * 上下文指针
     */
    public Object context = null;

    /**
     * 构造函数
     *
     * @param context
     */
    public CenterCallback(Object context) {
        this.context = context;
    }

    /**
     * 回调处理
     *
     * @param resultJSON
     */
    public void result(String resultJSON) {
        // 当退出中心后，回到游戏
        // 释放资源
        this.context = null;
        // End Method
    }

}
