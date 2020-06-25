package com.iccgame.sdk;

import android.app.Activity;

import com.iccgame.sdk.common.ICC_Log;
import com.iccgame.sdk.common.ICC_ScriptBuilder;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Administrator on 2015/9/21.
 */
public class ICC_SDKCallbacks extends ICC_SDKAssistiveTouch {
    /**
     * 回调绑定列表
     */
    protected HashMap<String, ICC_Callback> callbacks = new HashMap<String, ICC_Callback>();

    /**
     * 构造函数
     */
    protected ICC_SDKCallbacks(Activity activity) {
        // 继承父类
        super(activity);
    }


    /**
     * 注册回调
     *
     * @param callback
     * @return
     */
    synchronized String registerCallback(ICC_Callback callback) {
        // 生成标识
        String key = UUID.randomUUID().toString();
        // 暂存队列
        this.callbacks.put(key, callback);
        // 返回结果
        ICC_Log.info(String.format("HTML5 register callback %s", key));
        return ICC_ScriptBuilder.callback(key);
    }

    /**
     * 执行回调函数
     *
     * @param key
     * @param resultJSON
     * @return
     * @throws Exception
     */
    synchronized boolean executeCallback(String key, String resultJSON) {
        if (callbacks.containsKey(key) == false) {
            ICC_Log.warn(String.format("HTML5 callback %s not found.", key));
            return false;
        }
        ICC_Log.info(String.format("HTML5 callback %s %s", key, resultJSON));
        ICC_Callback callback = callbacks.remove(key);
        callback.result(resultJSON);
        ICC_Log.info(String.format("HTML5 callback %s removed.", key));
        return true;
    }

// End Class
}
