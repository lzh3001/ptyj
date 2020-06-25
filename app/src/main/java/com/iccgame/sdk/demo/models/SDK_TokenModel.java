package com.iccgame.sdk.demo.models;


public class SDK_TokenModel extends SDK_ModelAbstract {

    /**
     * 游戏账号标识
     */
    public String acct_game_user_id = "";

    /**
     * 账号状态
     */
    public String acct_is_persisted = "";

    /**
     * 登录时间
     */
    public String acct_login_time = "";

    /**
     * 获得游戏账号标识
     *
     * @return
     */
    public int getAcctGameUserId() {
        return Integer.parseInt(acct_game_user_id);
    }

    /**
     * 获得账号状态(true:正式用户; false:试玩用户)
     *
     * @return
     */
    public boolean getAcctIsPersisted() {
        return Integer.parseInt(acct_is_persisted) == 1;
    }

    /**
     * 获得登录请求时间
     *
     * @return
     */
    public long getAcctLoginTime() {
        return Integer.parseInt(acct_login_time);
    }

    /**
     * 获得模型实例
     *
     * @return
     */
    public static SDK_TokenModel factory(String resultQuery) {
        SDK_TokenModel model = new SDK_TokenModel();
        model.setValues(model.parseQuery(resultQuery));
        return model;
    }
    // End Class
}
