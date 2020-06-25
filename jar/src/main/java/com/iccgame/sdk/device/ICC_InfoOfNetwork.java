package com.iccgame.sdk.device;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.iccgame.sdk.ICC_SDK;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

/**
 * Created by Administrator on 2015/7/30.
 */
public class ICC_InfoOfNetwork extends ICC_InfoAbstract {

    /**
     * 没有网络
     */
    public static int NETWORKTYPE_INVALID = 0;

    /**
     * wap网络
     */
    public static int NETWORKTYPE_WAP = 1;

    /**
     * 2G网络
     */
    public static int NETWORKTYPE_2G = 2;

    /**
     * 3G 和 3G 以上网络，或统称为快速网络
     */
    public static int NETWORKTYPE_3G = 3;

    /**
     * wifi网络
     */
    public static int NETWORKTYPE_WIFI = 4;

    /**
     * 构造函数
     *
     * @param context
     */
    public ICC_InfoOfNetwork(Context context) {
        super(context);
    }

    /**
     * 获得网络类型
     *
     * @return
     */
    public int getNetworkType() {
        // 获得网络信息
        NetworkInfo networkInfo = ((ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        int mNetWorkType = NETWORKTYPE_INVALID;
        if (networkInfo == null || !networkInfo.isConnected()) {
            return mNetWorkType;
        }
        String type = networkInfo.getTypeName();
        if (type.equalsIgnoreCase("WIFI")) {
            mNetWorkType = NETWORKTYPE_WIFI;
        } else if (type.equalsIgnoreCase("MOBILE")) {
            String proxyHost = android.net.Proxy.getDefaultHost();
            mNetWorkType = TextUtils.isEmpty(proxyHost)
                    ? (this.isFastMobileNetwork(this.context) ? NETWORKTYPE_3G : NETWORKTYPE_2G)
                    : NETWORKTYPE_WAP;
        }
        return mNetWorkType;
    }

    /**
     * 获得物理网卡地址
     *
     * @return
     */
    public String getMACAddress() {
        String value = "";
        try {
            value = this.getNetAddresses().get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 获得物理网卡地址
     *
     * @return
     */
    public String getMACAddresses() {
        String value = "";
        try {
            value = TextUtils.join("\n", this.getNetAddresses());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 获得网卡地址
     *
     * @return
     */
    public ArrayList<String> getNetAddresses() {
        // 结果容器
        ArrayList<String> addresses = new ArrayList<String>();
        // 网卡配置路径
        File path = new File("sys/class/net/");
        File[] files = path.listFiles();
        if (files == null) {
            return addresses;
        }
        // 遍历每个配置
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.isDirectory() == false) {
                continue;
            }
            if (!file.getName().contains("wlan") && !file.getName().contains("eth")) {
                continue;
            }
            String address = readFile(file.getPath() + "/address");
            if (address == null) {
                continue;
            }
            addresses.add(address);
        }
        return addresses;
    }


    /**
     * 获取指定文件内容
     *
     * @param path
     * @return
     */
    protected static String readFile(String path) {
        String value = null;
        try {
            if ((new File(path)).exists()) {
                FileInputStream fis = new FileInputStream(path);
                byte[] buffer = new byte[8192];
                int byteCount = fis.read(buffer);
                if (byteCount > 0) {
                    value = new String(buffer, 0, byteCount, "utf-8").trim();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }


    /**
     * 3G和3G以上网络，或统称为快速网络，判断当前网络是否为快速网络
     *
     * @param context
     * @return
     */
    protected static boolean isFastMobileNetwork(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        switch (telephonyManager.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_1xRTT: // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_CDMA: // ~ 14-64 kbps
            case TelephonyManager.NETWORK_TYPE_EDGE: // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_GPRS: // ~ 100 kbps
            case TelephonyManager.NETWORK_TYPE_IDEN: // ~25 kbps
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return false;
//            case TelephonyManager.NETWORK_TYPE_EHRPD: // API11  ~ 1-2 Mbps
//            case TelephonyManager.NETWORK_TYPE_EVDO_0: // ~ 400-1000 kbps
//            case TelephonyManager.NETWORK_TYPE_EVDO_A: // ~ 600-1400 kbps
//            case TelephonyManager.NETWORK_TYPE_EVDO_B: // ~ 5 Mbps
//            case TelephonyManager.NETWORK_TYPE_HSDPA: // ~ 2-14 Mbps
//            case TelephonyManager.NETWORK_TYPE_HSPA: // ~ 700-1700 kbps
//            case TelephonyManager.NETWORK_TYPE_HSPAP: // API13 ~ 10-20 Mbps
//            case TelephonyManager.NETWORK_TYPE_HSUPA: // ~ 1-23 Mbps
//            case TelephonyManager.NETWORK_TYPE_LTE: // API11 ~ 10 Mbps+
//            case TelephonyManager.NETWORK_TYPE_UMTS: // ~ 400-7000 kbps
//            default:
//                return true;
        }
        return true;
    }
    // End Class
}
