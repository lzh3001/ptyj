package com.iccgame.sdk.common;

/**
 * Created by Administrator on 2015/11/3.
 */
public class ICC_DownloadListener {

    /**
     * 当任务首次开始调用
     *
     * @param task
     */
    public void onStart(ICC_Downloader.Task task) {

    }

    /**
     * 当任务删除调用
     *
     * @param task
     */
    public void onRemove(ICC_Downloader.Task task) {

    }

    /**
     * 当任务暂停下载调用
     *
     * @param task
     */
    public void onPause(ICC_Downloader.Task task) {

    }

    /**
     * 当任务恢复下载调用
     *
     * @param task
     */
    public void onResume(ICC_Downloader.Task task) {

    }

    /**
     * 当任务进度变更调用
     *
     * @param task
     */
    public void onProgress(ICC_Downloader.Task task) {

    }

    /**
     * 当任务下载完成调用
     *
     * @param task
     */
    public void onComplete(ICC_Downloader.Task task) {

    }
    // End Class
}
