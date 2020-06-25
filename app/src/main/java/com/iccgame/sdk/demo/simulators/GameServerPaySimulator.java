package com.iccgame.sdk.demo.simulators;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 * 支付服务器模拟代码
 * 这里只是一个演示
 * 请游戏根据情况重新编码
 *
 * @deprecated
 */
public class GameServerPaySimulator {

    /**
     * 接口地址
     */
    protected static final String INTERFACE_URL = "http://122.11.59.123/sdk_demo/PayOrderSign.php";

    /**
     * 内容缓冲
     */
    protected String buffer;

    /**
     * 模拟从服务器生成一个订单信息
     *
     * @param game_user_id
     * @return
     * @deprecated
     */
    public String newRandomTradeInfo(int commodityId, int game_user_id) {
        final String url = String.format("%s?game_user_id=%d&_=%d", INTERFACE_URL, game_user_id, System.currentTimeMillis());
        try {
            return getContent(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 解析订单部分数据
     *
     * @param resultJSON
     * @return
     */
    public String parseInfo(String resultJSON) {
        try {
            JSONObject data = new JSONObject(resultJSON);
            return data.getJSONObject("ICC_result").getString("pay_source");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 载入文档
     *
     * @param url
     * @return
     */
    public String getContent(final String url) {
        Log.i("SDK.DEMO", "GAME SERVER PAY ORDER " + url);
        this.buffer = "";
        // 独立线程
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpResponse httpResponse = (new DefaultHttpClient()).execute(new HttpGet(url));
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        buffer = EntityUtils.toString(httpResponse.getEntity());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        // 开始加载
        thread.start();
        // 等待完成
        while (thread.isAlive()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return this.buffer;
    }

    // End Class
}
