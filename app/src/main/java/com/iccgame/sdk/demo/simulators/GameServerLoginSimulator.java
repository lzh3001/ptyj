package com.iccgame.sdk.demo.simulators;

import com.iccgame.sdk.demo.models.SDK_TokenModel;

/**
 * 登录服务器模拟代码
 * 这里只是一个演示
 * 请游戏根据情况重新编码
 *
 * @deprecated
 */
public class GameServerLoginSimulator {

    /**
     * 模拟用户信息
     */
    private final SDK_TokenModel userInfo;

    /**
     * 构造函数
     *
     * @param model
     */
    private GameServerLoginSimulator(SDK_TokenModel model) {
        this.userInfo = model;
    }

    /**
     * 模拟游戏验证
     *
     * @param sdk_token
     * @return
     * @deprecated
     */
    public static GameServerLoginSimulator verify(String sdk_token) {
        SDK_TokenModel model = (SDK_TokenModel) SDK_TokenModel.factory(sdk_token);
        GameServerLoginSimulator simulator = new GameServerLoginSimulator(model);
        return simulator;
    }

    /**
     * 获得用户信息
     *
     * @return
     * @deprecated
     */
    public SDK_TokenModel getUserInfo() {
        return this.userInfo;
    }

    // End Class
}
