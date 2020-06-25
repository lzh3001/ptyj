package com.iccgame.sdk.device;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;

import com.iccgame.sdk.common.ICC_Log;

/**
 * Created by Administrator on 2015/7/30.
 */
public class ICC_InfoOfPhoneState extends ICC_InfoAbstract {

    /**
     * 构造函数
     *
     * @param context
     */
    public ICC_InfoOfPhoneState(Context context) {
        super(context);
    }

    /**
     * 获得 IMEI 码
     *
     * @return
     */
    public String getIMEI() {
        String value = "";
        if (this.checkPermission("android.permission.READ_PHONE_STATE") == false) {
            ICC_Log.warn("READ_PHONE_STATE permission denied.");
            return value;
        }
        try {
            // 获得管理器对象
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            // 导出结果
            value = telephonyManager.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 获得 SIM 服务商
     *
     * @return
     */
    public String getSimOperator() {
        String value = "";
        if (this.checkPermission("android.permission.READ_PHONE_STATE") == false) {
            ICC_Log.warn("READ_PHONE_STATE permission denied.");
            return value;
        }
        try {
            // 获得管理器对象
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            // 导出结果
            value = telephonyManager.getSimOperatorName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 获得 SIM 序号
     *
     * @return
     */
    public String getSimSerialNumber() {
        String value = "";
        if (this.checkPermission("android.permission.READ_PHONE_STATE") == false) {
            ICC_Log.warn("READ_PHONE_STATE permission denied.");
            return value;
        }
        try {
            // 获得管理器对象
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            // 导出结果
            value = telephonyManager.getSimSerialNumber();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    // End Class
}
