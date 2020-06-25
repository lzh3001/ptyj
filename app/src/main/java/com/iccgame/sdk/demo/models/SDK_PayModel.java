package com.iccgame.sdk.demo.models;


public class SDK_PayModel extends SDK_ModelAbstract {

    /**
     * ICCGAME 支付订单序号
     */
    public String trade_no = "";

    /**
     * 游戏订单序号
     */
    public String out_trade_no = "";


    /**
     * 获得ICCGAME订单序号
     *
     * @return
     */
    public String getTradeNo() {
        return trade_no;
    }

    /**
     * 获得游戏订单序号
     *
     * @return
     */
    public String getOutTradeNo() {
        return out_trade_no;
    }

    /**
     * 获得模型实例
     *
     * @return
     */
    public static SDK_PayModel factory(String resultJSON) {
        SDK_PayModel model = new SDK_PayModel();
        model.setValues(model.parseJSON(resultJSON));
        return model;
    }
    // End Class
}
