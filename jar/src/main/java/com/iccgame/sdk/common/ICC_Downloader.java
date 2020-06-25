package com.iccgame.sdk.common;

/**
 * Created by Administrator on 2015/11/3.
 */
public class ICC_Downloader {

    /**
     * 唯一实例
     */
    protected ICC_Downloader instance;

    /**
     * 处理监听
     */
    protected ICC_DownloadListener listener = new ICC_DownloadListener();

    /**
     * 构造函数
     */
    protected ICC_Downloader() {
    }

    /**
     * 获得实例指针
     *
     * @return
     */
    public ICC_Downloader getInstance() {
        if (this.instance == null) {
            this.instance = new ICC_Downloader();
        }
        return this.instance;
    }

    /**
     * 设置下载监听
     *
     * @param listener
     */
    public void setListener(ICC_DownloadListener listener) {
        this.listener = listener;
    }

    public String addTask(String url) {
        return "";
    }

    public String removeTask(String token) {
        return "";
    }

    public Task getTask(String token) {
        return new Task();
    }

    public Task[] getTasks() {
        return new Task[]{};
    }

    public boolean pauseTask(String token) {
        return false;
    }

    public boolean resumeTask(String token) {
        return false;
    }


    /**
     * 数据结构
     */
    public static class Task {

        /**
         * 状态 下载中
         */
        public static final int STATUS_DOWNLOADING = 1;

        /**
         * 状态 等待中
         */
        public static final int STATUS_WAIT = 2;

        /**
         * 索引编号
         */
        public String token;

        /**
         * 任务文件地址
         */
        public String url;

        /**
         * 任务文件大小
         */
        public long size;

        /**
         * 完成下载大小
         */
        public long loaded;

        /**
         * 下载状态
         */
        public int status;

        /**
         * 当前完成进度
         *
         * @return
         */
        public int getProgress() {
            return (int) (this.loaded / this.size * 100);
        }

        public static Task factory() {
            Task task = new Task();
            return task;
        }

        public static Task fromFile() {
            Task task = new Task();
            return task;
        }

        public void save() {
        }

        // End Struct
    }
    // End Class
}


//
//        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/3012_2_0_20151103.apk";
//        CRC32 crc32 = new CRC32();
//        try {
//            FileInputStream stream = new FileInputStream(path);
//            byte[] buffer = new byte[8 * 1024];
//            int length = 0;
//            while (0 < (length = stream.read(buffer, 0, buffer.length))) {
//                crc32.update(buffer, 0, length);
//            }
//            //CRC32: a6db3d5f
//            ICC_Log.info(String.format("CRC32: 0x%x", crc32.getValue()));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


//        URL url = new URL(urlstr);
//        HttpURLConnection connection = (HttpURLConnection) url
//                .openConnection();
//        connection.setConnectTimeout(5000);
//        connection.setRequestMethod("GET");
//        fileSize = connection.getContentLength();
//        File file = new File(localfile);
//        if (!file.exists()) {
//            file.createNewFile();
//        }
//        // 本地访问文件
//        RandomAccessFile accessFile = new RandomAccessFile(file, "rwd");
//        accessFile.setLength(fileSize);
//        accessFile.close();
//        connection.disconnect();
//
//        InputStream is = null;
//        try {
//            URL url = new URL(urlstr);
//            connection = (HttpURLConnection) url.openConnection();
//            connection.setConnectTimeout(5000);
//            connection.setRequestMethod("GET");
//            // 设置范围，格式为Range：bytes x-y;
//            connection.setRequestProperty("Range", "bytes=" + (startPos + compeleteSize) + "-" + endPos);
//            randomAccessFile = new RandomAccessFile(localfile, "rwd");
//            randomAccessFile.seek(startPos + compeleteSize);
//            // 将要下载的文件写到保存在保存路径下的文件中
//            is = connection.getInputStream();
//            byte[] buffer = new byte[4096];
//            int length = -1;
//            while ((length = is.read(buffer)) != -1) {
//                randomAccessFile.write(buffer, 0, length);
//                compeleteSize += length;
//                // 更新数据库中的下载信息
//                dao.updataInfos(threadId, compeleteSize, urlstr);
//                // 用消息将下载信息传给进度条，对进度条进行更新
//                if (state == PAUSE) {
//                    return;
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                is.close();
//                randomAccessFile.close();
//                connection.disconnect();
//                dao.closeDb();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
