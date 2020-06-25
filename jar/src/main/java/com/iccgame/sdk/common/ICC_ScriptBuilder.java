package com.iccgame.sdk.common;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/9/21.
 */
public class ICC_ScriptBuilder {

    /**
     * @param type
     * @return
     */
    public static String dispatchEvent(String type) {
        return dispatchEvent(type, "null");
    }

    /**
     * 抛出事件
     *
     * @param type
     * @param data
     * @return
     */
    public static String dispatchEvent(String type, JSONArray data) {
        return dispatchEvent(type, data.toString());
    }

    /**
     * @param type
     * @param data
     * @return
     */
    public static String dispatchEvent(String type, JSONObject data) {
        return dispatchEvent(type, data.toString());
    }

    /**
     * @param type
     * @param data
     * @return
     */
    public static String dispatchEvent(String type, String data) {
        StringBuilder script = new StringBuilder();
        script.append("var event = document.createEvent(\"HTMLEvents\");");
        script.append(String.format("event.initEvent(\"%s\", true, true);", type));
        if (data != null) {
            script.append(String.format("event.data = %s;", data));
        }
        script.append("document.body.dispatchEvent(event);");
        return script.toString();
    }

    /**
     * 抛出事件
     *
     * @param type
     * @return
     */
    public static String dispatchKeyEvent(String type, int code) {
        return dispatchKeyEvent(type, code, null);
    }

    /**
     * 抛出事件
     *
     * @param type
     * @param data
     * @return
     */
    public static String dispatchKeyEvent(String type, int code, JSONArray data) {
        StringBuilder script = new StringBuilder();
        script.append("var event = document.createEvent(\"HTMLEvents\");");
        script.append(String.format("event.initEvent(\"%s\", true, true);", type));
        script.append(String.format("event.keyCode = %d;", code));
        if (data != null) {
            script.append(String.format("event.data = %s;", data.toString()));
        }
        script.append("document.body.dispatchEvent(event);");
        return script.toString();
    }

    /**
     * @param key
     * @return
     */
    public static String callback(String key) {
        return String.format("function(result){prompt(\"ICCGAME_API:callback\",JSON.stringify([\"%s\",result]));}", key);
    }

    // End Class
}
