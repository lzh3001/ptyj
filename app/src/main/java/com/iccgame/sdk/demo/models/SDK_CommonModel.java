package com.iccgame.sdk.demo.models;


public class SDK_CommonModel extends SDK_ModelAbstract {

    /**
     * 获得模型实例
     *
     * @return
     */
    public static SDK_CommonModel factory(String resultJSON) {
        SDK_CommonModel model = new SDK_CommonModel();
        model.setValues(model.parseJSON(resultJSON));
        return model;
    }

    // End Class
}
