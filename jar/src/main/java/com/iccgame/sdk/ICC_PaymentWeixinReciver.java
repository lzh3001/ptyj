package com.iccgame.sdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.iccgame.sdk.common.ICC_Log;
import com.iccgame.sdk.common.ICC_ScriptBuilder;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/8/28.
 */
public class ICC_PaymentWeixinReciver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ICC_Log.info("ICC_PaymentWeixinReciver.onReceive(Context, Intent)");
        // 发送消息
        JSONObject params = new JSONObject();
        try {
            params.put("code", intent.getStringExtra("return_code"));
            params.put("message", intent.getStringExtra("return_msg"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ICC_SDK.getInstance().evalJavascript(ICC_ScriptBuilder.dispatchEvent("weixin_result", params));
    }

}
