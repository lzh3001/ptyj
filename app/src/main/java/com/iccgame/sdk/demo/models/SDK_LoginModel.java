package com.iccgame.sdk.demo.models;


public class SDK_LoginModel extends SDK_ModelAbstract {

    /**
     * 账号令牌
     */
    public String sdk_token = "";


    /**
     * 获得令牌信息
     *
     * @return
     */
    public String getToken() {
        return sdk_token;
    }

    /**
     * 获得模型实例
     *
     * @return
     */
    public static SDK_LoginModel factory(String resultJSON) {
        SDK_LoginModel model = new SDK_LoginModel();
        model.setValues(model.parseJSON(resultJSON));
        return model;
    }
    // End Class
}
