package com.iccgame.sdk.demo.callbacks;

import android.app.Activity;
import android.widget.Toast;

import com.iccgame.sdk.ICC_Callback;

abstract class CallbackAbstract implements ICC_Callback {

    /**
     * 操作指针
     */
    public Object context = null;

    /**
     * 构造函数
     *
     * @param context
     */
    public CallbackAbstract(Object context) {
        this.context = context;
    }

    /**
     * 显示汽包
     *
     * @param message
     */
    public void tip(final String message) {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText((Activity) context, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    // End Class
}
