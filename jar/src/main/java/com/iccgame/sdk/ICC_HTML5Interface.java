package com.iccgame.sdk;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.iccgame.sdk.common.ICC_AES;
import com.iccgame.sdk.common.ICC_Common;
import com.iccgame.sdk.common.ICC_Config;
import com.iccgame.sdk.common.ICC_Log;
import com.iccgame.sdk.device.ICC_InfoOfDevice;
import com.iccgame.sdk.device.ICC_InfoOfNetwork;
import com.iccgame.sdk.device.ICC_InfoOfPhoneState;

import org.json.JSONArray;

/**
 * 这是一个包内对象，不要添加public修饰符。
 */
class ICC_HTML5Interface {

    /**
     * 执行回调函数
     *
     * @param params
     * @return
     * @throws Exception
     */
    public void callback(final JSONArray params) {
        // 单起线程为了调整运行时机
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                ICC_SDK.getInstance().executeCallback(params.optString(0), params.optString(1));
//            }
//        }).start();
        new Handler(Looper.getMainLooper()).post(new Runnable() {
//        new Handler().post(new Runnable() {
            @Override
            public void run() {
                ICC_SDK.getInstance().executeCallback(params.optString(0), params.optString(1));
            }
        });
    }

    /**
     * 当HTML5准备完成后会进行回调
     */
    public void ready(JSONArray params) {
        ICC_SDK.getInstance().setActive(true);
        ICC_Log.info("HTML5 ready completed.");
    }

    /**
     * 创建窗体
     *
     * @return
     * @throws Exception
     */
    public boolean createActivity(JSONArray params) {
        return ICC_SDK.getInstance().createHtmlActivity();
    }

    /**
     * 销毁窗体
     *
     * @return
     * @throws Exception
     */
    public boolean finishActivity(JSONArray params) {
        return ICC_SDK.getInstance().finishHtmlActivity();
    }

    /**
     * 设置浮标
     *
     * @param params
     */
    public void setAssistiveTouch(JSONArray params) {
        ICC_SDK.getInstance().setAssistiveTouch((params == null) ? null : params.optString(0, null));
    }

    /**
     * 显示/隐藏 浮标
     *
     * @param params
     */
    public boolean setAssistiveTouchState(JSONArray params) {
        if (params.optBoolean(0)) {
            return ICC_SDK.getInstance().showAssistiveTouch();
        } else {
            return ICC_SDK.getInstance().hideAssistiveTouch();
        }
    }

    /**
     * 支付宝钱包支付
     *
     * @param params
     */
    public void payWithAlipay(JSONArray params) {
        // 调用支付宝充值
        new ICC_Payment().doPostAlipay(params.optString(0), ICC_SDK.getInstance().getGameActivity());
    }

    /**
     * 微信支付
     *
     * @param params
     */
    public void payWithWeixin(JSONArray params) {
        // 调用微信充值
        new ICC_Payment().doPostWeixin(params.optString(0), ICC_SDK.getInstance().getGameActivity());
    }

    /**
     * 文件是否存在
     *
     * @param params
     * @return
     */
    public int fileExists(JSONArray params) {
        return ICC_Common.fileExists(params.optString(0));
    }

    /**
     * AES 加密
     *
     * @param params
     * @return
     */
    public String aesEncrypt(JSONArray params) {
        return ICC_AES.encrypt(params.optString(0), params.optString(1));
    }

    /**
     * AES 解密
     *
     * @param params
     * @return
     */
    public String aesDecrypt(JSONArray params) {
        return ICC_AES.decrypt(params.optString(0), params.optString(1));
    }

    /**
     * 获得目录下的文件列表
     *
     * @param params
     * @return
     */
    public String getFiles(JSONArray params) {
        return ICC_Common.getFiles(params.optString(0));
    }

    /**
     * 读取文件
     *
     * @param params
     * @return
     */
    public String readFile(JSONArray params) throws Exception {
        return ICC_Common.readFile(params.optString(0));
    }

    /**
     * 写入文件
     *
     * @param params
     * @return
     */
    public boolean writeFile(JSONArray params) throws Exception {
        return ICC_Common.writeFile(params.optString(0), params.optString(1));
    }

    /**
     * 追加内容到文件
     *
     * @param params
     * @return
     * @throws Exception
     */
    public boolean appendFile(JSONArray params) throws Exception {
        return ICC_Common.appendFile(params.optString(0), params.optString(1));
    }

    /**
     * 删除文件
     *
     * @param params
     */
    public boolean deleteFile(JSONArray params) {
        return ICC_Common.deleteFile(params.optString(0));
    }

    /**
     * 弹出一个悬浮气泡
     *
     * @param params
     */
    public void tip(JSONArray params) {
        Toast.makeText(
                ICC_SDK.getInstance().getApplicationContext(), params.optString(0), Toast.LENGTH_SHORT
        ).show();
    }

    /**
     * 获取缓存地址
     *
     * @return
     */
    public String getPackageDataPath(JSONArray params) {
        return ICC_SDK.getInstance().getApplicationContext().getFilesDir()
                .getAbsolutePath();
    }

    /**
     * 获得包名称
     *
     * @param params
     * @return
     */
    public String getPackageName(JSONArray params) {
        return ICC_SDK.getInstance().getApplicationContext().getPackageName();
    }

    /**
     * 获得包版本
     *
     * @param params
     * @return
     */
    public String getPackageVersion(JSONArray params) {
        return new ICC_InfoOfDevice(ICC_SDK.getInstance().getApplicationContext()).getPackageVersion();
    }

    /**
     * 获得扩展地址
     *
     * @param params
     * @return
     */
    public String getExternalStoragePath(JSONArray params) {
        return new ICC_InfoOfDevice(ICC_SDK.getInstance().getApplicationContext()).getExternalStoragePath();
    }

    /**
     * 获得图片地址
     *
     * @param params
     * @return
     */
    public String getPicturePath(JSONArray params) {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
    }

    /**
     * 获得下载地址
     *
     * @param params
     * @return
     */
    public String getDownloadPath(JSONArray params) {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
    }

    /**
     * 获得设备名称
     *
     * @return
     */
    public String getDevice(JSONArray params) {
        return Build.MODEL;
    }

    /**
     * 获得IMEI
     *
     * @return
     */
    public String getIMEI(JSONArray params) {
        return new ICC_InfoOfPhoneState(ICC_SDK.getInstance().getApplicationContext()).getIMEI();
    }

    /**
     * 获得用户安装的软件
     *
     * @return
     */
    public String getInstalledPackages(JSONArray params) {
        return new ICC_InfoOfDevice(ICC_SDK.getInstance().getApplicationContext()).getInstalledPackages();
    }

    /**
     * 获得网卡地址
     *
     * @return
     */
    public String getMACAddress(JSONArray params) {
        return new ICC_InfoOfNetwork(ICC_SDK.getInstance().getApplicationContext()).getMACAddress();
    }

    /**
     * 获得多网卡地址
     *
     * @return
     */
    public String getMACAddresses(JSONArray params) {
        return new ICC_InfoOfNetwork(ICC_SDK.getInstance().getApplicationContext()).getMACAddresses();
    }

    /**
     * 获得网络类型
     *
     * @return
     */
    public int getNetworkType(JSONArray params) {
        return new ICC_InfoOfNetwork(ICC_SDK.getInstance().getApplicationContext()).getNetworkType();
    }

    /**
     * 获得序列号
     *
     * @return
     */
    public String getSerialNumber(JSONArray params) {
        return Build.SERIAL;
    }

    /**
     * 获得SIM供应商
     *
     * @return
     */
    public String getSimOperator(JSONArray params) {
        return new ICC_InfoOfPhoneState(ICC_SDK.getInstance().getApplicationContext()).getSimOperator();
    }

    /**
     * 获得SIM序列号
     *
     * @return
     */
    public String getSimSerialNumber(JSONArray params) {
        return new ICC_InfoOfPhoneState(ICC_SDK.getInstance().getApplicationContext()).getSimSerialNumber();
    }

    /**
     * 获得系统名称
     *
     * @return
     */
    public String getSystem(JSONArray params) {
        return "Android";
    }

    /**
     * 获得系统版本
     *
     * @return
     */
    public String getSystemVersion(JSONArray params) {
        return Build.VERSION.RELEASE;
    }

    /**
     * 唤出拨号界面
     *
     * @param params
     */
    public void callPhone(JSONArray params) {
        ICC_Common.callPhone(ICC_SDK.getInstance().getApplicationContext(), params.optString(0));
    }

    /**
     * 唤出短信界面
     *
     * @param params
     */
    public void sendMessage(JSONArray params) {
        ICC_Common.sendMessage(ICC_SDK.getInstance().getApplicationContext(), params.optString(0), params.optString(1));
    }

    /**
     * 唤出浏览器界面
     *
     * @param params
     */
    public void openBrowser(JSONArray params) {
        ICC_Common.openBrowser(ICC_SDK.getInstance().getApplicationContext(), params.optString(0));
    }

    /**
     * 安装软件
     *
     * @param params
     */
    public void installPackageArchive(JSONArray params) {
        ICC_Common.installPackageArchive(ICC_SDK.getInstance().getApplicationContext(), params.optString(0));
    }

    /**
     * 卸载软件
     *
     * @param params
     */
    public boolean uninstallPackage(JSONArray params) {
        return ICC_Common.uninstallPackage(ICC_SDK.getInstance().getApplicationContext(), params.optString(0));
    }

    /**
     * 获得SDK版本
     *
     * @return
     */
    public String getVersion(JSONArray params) {
        return ICC_Config.SDK_VERSION;
    }

    // End Class
}
