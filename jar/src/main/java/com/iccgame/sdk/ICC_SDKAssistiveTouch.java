package com.iccgame.sdk;

import android.app.Activity;
import android.graphics.Point;

import com.iccgame.sdk.common.ICC_AssistiveTouch;
import com.iccgame.sdk.common.ICC_AssistiveTouchListener;
import com.iccgame.sdk.common.ICC_Config;
import com.iccgame.sdk.common.ICC_Log;
import com.iccgame.sdk.common.ICC_ScriptBuilder;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * Created by Administrator on 2015/10/29.
 */
public class ICC_SDKAssistiveTouch extends ICC_SDKActivity {

    /**
     * 助手指针
     */
    protected ICC_AssistiveTouch _assistiveTouch = null;

    /**
     * 构造函数
     */
    protected ICC_SDKAssistiveTouch(Activity activity) {
        // 继承父类
        super(activity);
    }

    /**
     * 设置浮标
     *
     * @param image
     * @return
     */
    boolean setAssistiveTouch(String image) {
        if (image == null) {
            if (this._assistiveTouch == null) {
                ICC_Log.warn("ICC_SDKAssistiveTouch.getAssistiveTouch() is null");
                return false;
            }
            ICC_Log.info("AssistiveTouch destroy.");
            this._assistiveTouch.hide();
            this._assistiveTouch = null;
            return true;
        } else {
            if (this._assistiveTouch != null) {
                ICC_Log.warn("ICC_SDKAssistiveTouch.getAssistiveTouch() is not null");
                return false;
            }
            ICC_Log.info("AssistiveTouch create.");
            this._assistiveTouch = new ICC_AssistiveTouch(this.getGameActivity(), image, this.readPosition());
            this._assistiveTouch.setTouchListener(new ICC_SDKAssistiveTouch.Listener());
            return this._assistiveTouch.show();
        }
    }

    /**
     * 获得浮标
     *
     * @return
     */
    ICC_AssistiveTouch getAssistiveTouch() {
        return this._assistiveTouch;
    }

    /**
     * 显示浮标
     *
     * @return
     */
    boolean showAssistiveTouch() {
        ICC_Log.info("ICC_SDKAssistiveTouch.showAssistiveTouch()");
        if (this.getAssistiveTouch() == null) {
            ICC_Log.warn("ICC_SDKAssistiveTouch.getAssistiveTouch() is null");
            return false;
        }
        return this.getAssistiveTouch().show();
    }

    /**
     * 隐藏浮标
     *
     * @return
     */
    boolean hideAssistiveTouch() {
        ICC_Log.info("ICC_SDKAssistiveTouch.hideAssistiveTouch()");
        if (this.getAssistiveTouch() == null) {
            ICC_Log.warn("ICC_SDKAssistiveTouch.getAssistiveTouch() is null");
            return false;
        }
        return this.getAssistiveTouch().hide();
    }

    /**
     * 监听事件处理
     */
    protected class Listener extends ICC_AssistiveTouchListener {
        /**
         * 触及
         *
         * @param context
         */
        public void onTouch(ICC_AssistiveTouch context) {
            ICC_Log.info("AssistiveTouch touch.");
            ICC_SDKAssistiveTouch.this.evalJavascript(ICC_ScriptBuilder.dispatchEvent("assistive_touch"));
        }

        /**
         * 位置发生变化
         *
         * @param context
         * @param position
         */
        public void onMoved(ICC_AssistiveTouch context, Point position) {
            ICC_Log.info(String.format("AssistiveTouch moved, x:%d, y:%d", position.x, position.y));
            // 保存历史
            savePosition(position);
        }
        // End Sub Class
    }

    /**
     * 保存位置
     *
     * @param position
     */
    protected void savePosition(Point position) {
        ICC_Log.debug(String.format("ICC_SDKAssistiveTouch.savePosition(Point), x:%d, y:%d", position.x, position.y));
        String path = this.getDatPath();
        try {
            // 首次创建
            File file = new File(path);
            if (file.exists() == false) {
                File parent = file.getParentFile();
                if (parent.exists() == false) {
                    parent.mkdirs();
                }
                file.createNewFile();
            }
            // 写入文件
            RandomAccessFile stream = new RandomAccessFile(file, "rw");
            stream.writeInt(position.x);
            stream.writeInt(position.y);
            // 释放资源
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取位置
     *
     * @return
     */
    protected Point readPosition() {
        ICC_Log.debug(String.format("ICC_SDKAssistiveTouch.readPosition()"));
        Point position = new Point();
        String path = this.getDatPath();
        try {
            // 首次创建
            File file = new File(path);
            if (file.exists() == false) {
                return null;
            }
            // 去读文件
            RandomAccessFile stream = new RandomAccessFile(file, "r");
            position.x = stream.readInt();
            position.y = stream.readInt();
            // 释放资源
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
            position = null;
        }
        return position;
    }

    /**
     * 获得数据路径
     *
     * @return
     */
    protected String getDatPath() {
        String path = this.getApplicationContext().getFilesDir()
                .getAbsolutePath() + ICC_Config.SDK_FOLDER + "/assistive_touch.dat";
        ICC_Log.debug(String.format("ICC_SDKAssistiveTouch.getDatPath(), %s", path));
        return path;
    }

    // End Class
}
