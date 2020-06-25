package com.iccgame.sdk.demo;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CalendarContract;
import android.view.View;

import com.iccgame.sdk.ICC_SDK;
import com.iccgame.sdk.JniHelper;
import com.iccgame.sdk.demo.callbacks.CenterCallback;
import com.iccgame.sdk.demo.callbacks.LoginCallback;
import com.iccgame.sdk.demo.callbacks.PayCallback;
import com.iccgame.sdk.demo.callbacks.QuitCallback;
import com.iccgame.sdk.demo.callbacks.SwitchUserCallback;
import com.iccgame.sdk.demo.models.SDK_TokenModel;
import com.iccgame.sdk.demo.simulators.GameServerPaySimulator;

import java.io.File;

/**
 * SDK 所有接口的调用方法将在该类中一一演示
 */
public class MainActivity extends Activity {

    static {
        System.loadLibrary("ICC_SDK");
    }

    /**
     * 账号信息，请游戏客户端填写自己的代码
     *
     * @param savedInstanceState
     */
    public SDK_TokenModel tmpUserInfo = null;

    /**
     * 窗口创建回调
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 继承父类
        super.onCreate(savedInstanceState);
        // 背景颜色
        this.getWindow().setBackgroundDrawable(new ColorDrawable(0xFFEEEEEE));
        // 欢迎屏幕
        Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION + Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivityForResult(intent, 0);
        // 初始 SDK
        ICC_SDK.getInstance(this);
        // 显示内容
        setContentView(R.layout.activity_main);
    }

    /**
     * 析构调用
     */
    @Override
    protected void onDestroy() {
        // 析构 SDK
        ICC_SDK.getInstance().destroy();
        // 继承父类
        super.onDestroy();
    }

    /**
     * 触击登录按钮
     *
     * @param v
     * @return
     */
    public void onClickLogin(View v) {
        // 唤出登录界面
        ICC_SDK.getInstance().login(new LoginCallback(this));
//        JniHelper.tester(JniHelper.METHOD_LOGIN, "");
    }

    /**
     * 触击支付按钮
     *
     * @param v
     * @return
     */
    public void onClickPay(View v) {
        // 购买商品编号
        int commodity_id = 17;
        // 游戏账号标识
        int game_user_id = 30;
        if (this.tmpUserInfo != null) {
            game_user_id = this.tmpUserInfo.getAcctGameUserId();
        }
        // 模拟生成订单
        String tradeInfo = (new GameServerPaySimulator()).newRandomTradeInfo(commodity_id, game_user_id);
        // 唤出支付
        ICC_SDK.getInstance().pay(tradeInfo, new PayCallback(this));
        //JniHelper.tester(JniHelper.METHOD_PAY, tradeInfo);
    }

    /**
     * 触击注销按钮
     *
     * @param v
     */
    public void onClickQuit(View v) {
        ICC_SDK.getInstance().logout(new QuitCallback(this));
    }

    /**
     * 触击切换用户按钮
     *
     * @param v
     * @return
     */
    public void onClickSwitchUser(View v) {
        // 注销登录
        ICC_SDK.getInstance().logout(new SwitchUserCallback(this));
    }

    /**
     * 触击账号中心按钮
     *
     * @param v
     * @return
     */
    public void onClickCenter(View v) {
        ICC_SDK.getInstance().center(new CenterCallback(this));
    }

    /**
     * @param v
     */
    public void onClickKill(View v) {
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * @param v
     */
    public void onClickClear(View v) {
        String pathEx = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.ICCGAME.assist";
        File fileEx = new File(pathEx);
        if (fileEx.exists()) {
            RecursionDeleteFile(fileEx);
        }
        String path = this.getApplicationContext().getFilesDir()
                .getAbsolutePath() + "/ICCGAME_SDK";
        File file = new File(path);
        if (file.exists()) {
            RecursionDeleteFile(file);
        }
        this.onClickKill(v);
    }

    public static boolean RecursionDeleteFile(File file) {
        if (file.isFile()) {
            return file.delete();
        } else if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                return file.delete();
            }
            for (File f : childFile) {
                RecursionDeleteFile(f);
            }
            return file.delete();
        }
        return false;
    }

    /**
     * 两次返回则退出游戏
     *
     * @param keyCode
     * @param event
     * @return
     */
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        //返回键
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            finish();
//            System.exit(0);
//        }
//        return true;
//    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.onClickLogin(null);
    }
    // End Class
}
