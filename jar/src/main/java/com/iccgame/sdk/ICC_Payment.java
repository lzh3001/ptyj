package com.iccgame.sdk;

import android.app.Activity;
import android.content.pm.PackageInfo;

import com.alipay.sdk.app.PayTask;
import com.iccgame.sdk.common.ICC_Log;
import com.iccgame.sdk.common.ICC_ScriptBuilder;
import com.paytend.safepayplug.SafePayConstans;
import com.paytend.safepayplug.WXpay;

import org.json.JSONObject;

class ICC_Payment {

    /**
     * 调用支付宝接口
     *
     * @param tradeInfo
     */
    public void doPostAlipay(final String tradeInfo, final Activity activity) {
        ICC_Log.debug(String.format("post alipay sdk %s", tradeInfo));
        // 支付宝自身做了死循环，阻塞线程，直到玩家操作结束
        // 所以必须建立新的线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(activity);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(tradeInfo);
                // 告知SDK渠道支付完成
                String params = "\"" + result.replace("\"", "\\\"") + "\"";
                ICC_SDK.getInstance().evalJavascript(ICC_ScriptBuilder.dispatchEvent("alipay_result", params));
            }
        }).start();
    }

    /**
     * 调用微信接口
     *
     * @param tradeInfo
     */
    public void doPostWeixin(final String tradeInfo, final Activity activity) {
        ICC_Log.debug(String.format("post weixin sdk %s", tradeInfo));
        try {
            Paytend paytend = new Paytend();
            PackageInfo weixin = paytend.isAvilible(activity, "com.tencent.mm");
            if (null == weixin) {
                ICC_Log.warn("Weixin not found.");
                // 发送消息
                JSONObject params = new JSONObject();
                params.put("code", "WEIXIN_NOT_FOUND");
                params.put("message", "没有安装微信");
                ICC_SDK.getInstance().evalJavascript(ICC_ScriptBuilder.dispatchEvent("weixin_result", params));
                return;
            } else if (weixin.versionCode < 500) {
                ICC_Log.warn("Weixin version too low.");
                // 发送消息
                JSONObject params = new JSONObject();
                params.put("code", "WEIXIN_VERSION_TOO_LOW");
                params.put("message", "微信版本过低");
                ICC_SDK.getInstance().evalJavascript(ICC_ScriptBuilder.dispatchEvent("weixin_result", params));
                return;
            }
            if (null == paytend.isAvilible(activity, SafePayConstans.pakeageName)) {
                ICC_Log.info("Install " + SafePayConstans.pakeageName);
                // 安装软件
                paytend.install(activity, paytend.export());
                // 发送消息
                JSONObject params = new JSONObject();
                params.put("code", "PLUGIN_INSTALLATION");
                params.put("message", "正在安装安全插件");
                ICC_SDK.getInstance().evalJavascript(ICC_ScriptBuilder.dispatchEvent("weixin_result", params));
                return;
            }
            JSONObject params = new JSONObject(tradeInfo);
            WXpay mWXpay = WXpay.getWxpay(activity);
            mWXpay.Start(
                    params.optString("merchantId"), // 商户号
                    params.optString("out_trade_no"), // 商户订单号
                    params.optString("total_fee"), // 订单金额
                    params.optString("sub_mch_notify_url"), // 通知地址
                    params.optString("body"), // 商品描述
                    params.optString("nonce_str"), // 随机字符串
                    params.optString("sign") // 签名
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // End Class
}
