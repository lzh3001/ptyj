package com.iccgame.sdk;

import android.webkit.ConsoleMessage;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.iccgame.sdk.common.ICC_Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/9/21.
 */
class ICC_ChromeClient extends WebChromeClient {

    /**
     * 会话接口
     */
    public final ICC_HTML5Interface htmlApi = new ICC_HTML5Interface();

    /**
     * @param consoleMessage
     * @return
     */
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        // 继承父类
        super.onConsoleMessage(consoleMessage);
        // 判断类型
        if (consoleMessage.messageLevel() == ConsoleMessage.MessageLevel.ERROR) {
            ICC_Log.error(String.format("HTML5 %s in %s line %d", consoleMessage.message(), consoleMessage.sourceId(), consoleMessage.lineNumber()));
        } else if (consoleMessage.messageLevel() == ConsoleMessage.MessageLevel.WARNING) {
            ICC_Log.warn(String.format("HTML5 %s in %s line %d", consoleMessage.message(), consoleMessage.sourceId(), consoleMessage.lineNumber()));
        } else if (consoleMessage.messageLevel() == ConsoleMessage.MessageLevel.LOG) {
            ICC_Log.debug(String.format("HTML5 %s", consoleMessage.message()));
        } else {
            return false;
        }
        return true;
    }

    /**
     * @param view
     * @param url
     * @param message
     * @param defaultValue
     * @param result
     * @return
     */
    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        try {
            Pattern pattern = Pattern.compile("^ICCGAME_API:([a-z_][a-z0-9_]*)$", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(message);
            if (matcher.find() == false) {
                throw new RuntimeException("HTML5 unknown command. " + message);
            }
            Method method = this.htmlApi.getClass().getDeclaredMethod(matcher.group(1), new Class[]{JSONArray.class});
            JSONArray params = null;
            try {
                if (defaultValue != null && defaultValue.length() > 0) {
                    params = new JSONArray(defaultValue);
                }
            } catch (JSONException e) {
                throw new RuntimeException(String.format("HTML5 parameters invalid. %s(%s)", matcher.group(1), defaultValue));
            }
            String apiResult = String.valueOf(method.invoke(this.htmlApi, params));
            ICC_Log.debug(String.format("HTML5 API call %s(%s) return %s", matcher.group(1), defaultValue, apiResult));
            result.confirm(apiResult);
        } catch (Exception e) {
            ICC_Log.warn(e.getMessage());
            return false;
        }
        return true;
    }

    // End Class
}
