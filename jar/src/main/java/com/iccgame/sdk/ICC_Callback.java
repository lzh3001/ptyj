package com.iccgame.sdk;

/**
 * Created by ZLei on 2015/6/30.
 */
public interface ICC_Callback {
    /**
     * 操作结果
     * { "sdk_result":-3102, "sdk_message":"玩家取消" }
     * { "sdk_result":-3101, "sdk_message":"操作被打断", sdk_active:%1$d }
     * { "sdk_result":0, "sdk_message":"登录成功", "sdk_token":"%1$s" }
     * { "sdk_result":0, "sdk_message":"注册成功", "sdk_token":"%1$s" }
     * { "sdk_result":0, "sdk_message":"支付成功", "sdk_serial":"%1$s" }
     * { "sdk_result":0, "sdk_message":"操作完成" }
     * { "sdk_result":0, "sdk_message":"登出成功" }
     *
     * @param resultJSON
     */
    void result(String resultJSON);
}
