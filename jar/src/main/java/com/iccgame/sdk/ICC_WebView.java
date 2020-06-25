package com.iccgame.sdk;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.iccgame.sdk.common.ICC_Config;
import com.iccgame.sdk.common.ICC_Log;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * Created by Administrator on 2015/9/21.
 */
class ICC_WebView extends WebView {

    /**
     * 配置文件名称
     */
    public static final String CONFIG_PATH = "ICC_SDK.conf";

    /**
     * 调试模式
     */
    public boolean debuggable = false;


    /**
     * 构造函数
     *
     * @param context
     */
    public ICC_WebView(Context context) {
        // 继承父类
        super(context);
        // 调试模式
        this.debuggable = (0 != (this.getContext().getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE));
        // 初始父类
        ICC_Log.info("WebView Initialization");
        this.initializeSuper();
        // 载入数据
        this.loadDefultData();
    }

    /**
     * 初始父类
     */
    protected void initializeSuper() {
        // 初始设置
        WebSettings settings = this.getSettings();
        // 设置 访问本地文件
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            settings.setAllowContentAccess(true);
        }
        settings.setAllowFileAccess(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            settings.setAllowFileAccessFromFileURLs(true);
            settings.setAllowUniversalAccessFromFileURLs(true);
        }
        settings.setAppCacheEnabled(true);
        settings.setUserAgentString(settings.getUserAgentString() + String.format(" ICCGAME SDK/%s", ICC_Config.SDK_VERSION));
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            settings.setAppCacheMaxSize(32 * 1024 * 1024);
        }
        settings.setAppCachePath(this.getCachePath() + "/webviewCache");
        settings.setBlockNetworkImage(false);
        settings.setBlockNetworkLoads(false);
        settings.setBuiltInZoomControls(false);
        settings.setCacheMode(this.debuggable ? WebSettings.LOAD_NO_CACHE : WebSettings.LOAD_DEFAULT);
        settings.setDatabaseEnabled(true);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            settings.setDatabasePath(this.getCachePath() + "/webviewDatabase");
        }
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setDomStorageEnabled(true);
        settings.setGeolocationDatabasePath(this.getCachePath() + "/webviewCeolocation");
        settings.setGeolocationEnabled(true);
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(false);
        // 属性赋值
        this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        this.setWebChromeClient(new ICC_ChromeClient());
        this.setWebViewClient(new WebViewClient());
    }

    /**
     * 载入默认数据
     */
    public void loadDefultData() {
        JSONObject object = this.getAssetJSON();
        // 属性赋值
        String remoteUrl = object.optString("remoteUrl", "");
        if (remoteUrl == null || remoteUrl.length() < 1) {
            remoteUrl = ICC_Config.SDK_REMOTE_URL;
        }
        // 属性赋值
        String localCode = object.optString("localCode", "");
        if (localCode != null && remoteUrl.length() > 0) {
            try {
                localCode = new String(Base64.decode(localCode, Base64.DEFAULT), "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
                localCode = "";
            }
        }
        if (localCode == null || localCode.length() < 1) {
            ICC_Log.debug(String.format("ICC_WebView.loadUrl(%s)", remoteUrl));
            this.loadUrl(remoteUrl);
        } else {
            ICC_Log.debug(String.format("ICC_WebView.loadDataWithBaseURL(%s, String(HASH:%s, SIZE:%d), \"text/html\", \"UTF-8\", null); ", remoteUrl, this.hash(localCode), localCode.length()));
            this.loadDataWithBaseURL(remoteUrl, localCode, "text/html", "UTF-8", null);
        }
    }

    /**
     * 执行 JAVASCRIPT 代码
     *
     * @param script
     */
    public synchronized void evalJavascript(final String script) {
        // 不要更改调用方式 Handler
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            /**
             * 执行代码
             */
            @Override
            public void run() {
                try {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                        ICC_Log.debug(String.format("ICC_WebView.loadUrl(javascript: %s)", script));
                        loadUrl("javascript: " + script);
                    } else {
                        ICC_Log.debug(String.format("ICC_WebView.evaluateJavascript(%s)", script));
                        evaluateJavascript(script, new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 获得资源文件
     *
     * @return
     */
    protected JSONObject getAssetJSON() {
        try {
            InputStream stream = ICC_SDK.getInstance().getApplicationContext().getAssets().open(CONFIG_PATH);
            ByteArrayOutputStream data = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int count = -1;
            while ((count = stream.read(buffer, 0, buffer.length)) > 0) {
                data.write(buffer, 0, count);
            }
            return new JSONObject(data.toString("utf-8"));
        } catch (Exception e) {
            ICC_Log.warn(String.format("Asset %s not found.", CONFIG_PATH));
        }
        return new JSONObject();
    }

    /**
     * 获得缓存所在路径
     *
     * @return
     */
    public String getCachePath() {
        String path = ICC_SDK.getInstance().getApplicationContext().getFilesDir()
                .getAbsolutePath() + ICC_Config.SDK_FOLDER;
        ICC_Log.debug(String.format("ICC_WebView.getCachePath() return %s", path));
        return path;
    }

    /**
     * 哈希数据
     *
     * @param value
     * @return
     */
    public String hash(String value) {
        StringBuilder hash = new StringBuilder(64);
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(value.getBytes("UTF-8"));
            byte[] bytes = md5.digest();//加密
            for (byte b : bytes) {
                if ((b & 0xFF) < 0x10) hash.append("0");
                hash.append(Integer.toHexString(b & 0xFF));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hash.toString();
    }

    // End Class
}
