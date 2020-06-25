package com.iccgame.sdk.demo.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

abstract class SDK_ModelAbstract {

    /**
     * 结果代码
     */
    public String sdk_result = "";

    /**
     * 结果信息
     */
    public String sdk_message = "";

    /**
     * 获得结果代码
     *
     * @return
     */
    public int getCode() {
        return Integer.valueOf(sdk_result);
    }

    /**
     * 获得结果消息
     *
     * @return
     */
    public String getMessage() {
        return sdk_message;
    }

    /**
     * 解析数据
     *
     * @param value
     * @return
     */
    public HashMap<String, String> parseJSON(String value) {
        if (null == value && value.length() < 8) {
            return null;
        }
        // 解析数据
        JSONObject params;
        try {
            params = new JSONObject(value);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        // 导出参数
        HashMap<String, String> properties = new HashMap<String, String>();
        Iterator<String> iterator = params.keys();
        while (iterator.hasNext()) {
            try {
                String k = iterator.next();
                properties.put(k, params.getString(k));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 实例对象
        return properties;
    }

    /**
     * 解析数据
     *
     * @param value
     * @return
     */
    public HashMap<String, String> parseQuery(String value) {
        if (null == value && value.length() < 3) {
            return null;
        }
        // 导出参数
        HashMap<String, String> properties = new HashMap<String, String>();
        String[] params = value.split("&");
        for (int i = 0; i < params.length; i++) {
            int offset = params[i].indexOf("=");
            properties.put(params[i].substring(0, offset), params[i].substring(offset + 1));
        }
        // 实例对象
        return properties;
    }

    /**
     * 实例对象并赋值
     *
     * @param properties
     * @return
     */
    protected void setValues(Map<String, String> properties) {
        // 识别结构
        Field[] fields = this.getClass().getFields();
        // 循环导出
        for (int i = 0; i < fields.length; i++) {
            try {
                fields[i].set(this, properties.get(fields[i].getName()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    // End Class
}