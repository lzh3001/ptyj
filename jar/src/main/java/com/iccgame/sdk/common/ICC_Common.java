package com.iccgame.sdk.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;

import com.iccgame.sdk.ICC_SDK;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ICC_Common {

    /**
     * 判断文件是否存在
     *
     * @param path
     * @return
     */
    public static int fileExists(String path) {
        File file = new File(path);
        if (file.exists()) {
            if (file.isDirectory()) {
                return 2;
            } else {
                return 1;
            }
        }
        return 0;
    }

    /**
     * 获得文件夹内的文件
     *
     * @param filename
     * @return
     */
    public static String getFiles(String filename) {
        File file = new File(filename);
        if (file.exists() == false) {
            ICC_Log.warn(String.format("ICC_Common.getFiles(%s) not exists.", filename));
            return "";
        }
        if (file.isDirectory() == false) {
            ICC_Log.warn(String.format("ICC_Common.getFiles(%s) is not directory.", filename));
            return "";
        }
        List<String> items = new ArrayList<String>();
        for (File item : file.listFiles()) {
            items.add(item.getAbsolutePath());
        }
        return TextUtils.join("\n", items);
    }

    /**
     * 读文件
     *
     * @param filename
     * @return
     */
    public static String readFile(String filename) {
        // 不存在，则返回空
        if ((new File(filename)).exists() == false) {
            ICC_Log.warn(String.format("ICC_Common.readFile(%s) not exists.", filename));
            return "";
        }
        try {
            FileInputStream stream = new FileInputStream(filename);
            int size = (int) stream.getChannel().size();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            return new String(buffer);
        } catch (Exception e) {
            ICC_Log.warn(String.format("ICC_Common.readFile(%s) read error. %s", filename, e.getMessage()));
        }
        return "";
    }

    /**
     * 写文件
     *
     * @param filename
     * @param contents
     * @return
     */
    public static boolean writeFile(String filename, String contents) {
        try {
            File file = new File(filename);
            if (file.exists() == false) {
                File parent = file.getParentFile();
                if (parent.exists() == false) {
                    parent.mkdirs();
                }
                file.createNewFile();
            }
            FileOutputStream outputStream = new FileOutputStream(filename, false);
            outputStream.write(contents.getBytes());
            outputStream.flush();
            outputStream.close();
            return true;
        } catch (Exception e) {
            ICC_Log.warn(String.format("ICC_Common.writeFile(%s, String) write error. %s", filename, e.getMessage()));
        }
        return false;
    }

    /**
     * 追加文件
     *
     * @param filename
     * @param contents
     * @return
     * @throws IOException
     */
    public static boolean appendFile(String filename, String contents) {
        try {
            File file = new File(filename);
            if (file.exists() == false) {
                File parent = file.getParentFile();
                if (parent.exists() == false) {
                    parent.mkdirs();
                }
                file.createNewFile();
            }
            FileOutputStream outputStream = new FileOutputStream(filename, true);
            outputStream.write(contents.getBytes());
            outputStream.flush();
            outputStream.close();
            return true;
        } catch (Exception e) {
            ICC_Log.warn(String.format("ICC_Common.appendFile(%s, String) write error. %s", filename, e.getMessage()));
        }
        return false;
    }

    /**
     * 删除文件
     *
     * @param filename
     * @return
     */
    public static boolean deleteFile(String filename) {
        try {
            File file = new File(filename);
            if (file.exists() == false) {
                return true;
            }
            return file.delete();
        } catch (Exception e) {
            ICC_Log.warn(String.format("ICC_Common.deleteFile(%s) delete error. %s", filename, e.getMessage()));
        }
        return false;
    }

    /**
     * 唤出手机拨号
     *
     * @param context
     * @param number
     */
    public static void callPhone(Context context, String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 唤出短信界面
     *
     * @param context
     * @param number
     */
    public static void sendMessage(Context context, String number, String message) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + number));
        intent.putExtra("sms_body", message);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 唤出浏览器
     *
     * @param context
     * @param url
     */
    public static void openBrowser(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }

    /**
     * 安装apk
     *
     * @param context
     * @param path
     */
    public static void installPackageArchive(Context context, String path) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(
                Uri.parse("file://" + path),
                "application/vnd.android.package-archive"
        );
        context.startActivity(intent);
    }

    /**
     * 卸载应用
     *
     * @param context
     * @param appName
     */
    public static boolean uninstallPackage(Context context, String appName) {
        String packageName = null;
        // 获得管理器对象
        PackageManager packageManager = context.getPackageManager();
        // 遍历安装软件
        List<PackageInfo> packages = packageManager.getInstalledPackages(0);
        for (PackageInfo item : packages) {
            if ((item.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                continue;
            }
            String label = item.applicationInfo.loadLabel(packageManager).toString();
            if (appName == label) {
                packageName = item.packageName;
            }
        }
        if (packageName == null) {
            return false;
        }
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse(packageName));
        context.startActivity(intent);
        return true;
    }

    // End Class
}
