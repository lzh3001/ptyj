package com.iccgame.sdk.common;

import android.graphics.Point;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.Date;

/**
 * 惯性采样计算器
 */
public class ICC_VelocityTracker {

    /**
     * 预设方向：上
     */
    public static final int TOP = 0;

    /**
     * 预设方向：右
     */
    public static final int RIGHT = 1;

    /**
     * 预设方向：下
     */
    public static final int BOTTOM = 2;

    /**
     * 预设方向：左
     */
    public static final int LEFT = 3;

    /**
     * 采样数量
     */
    public int maxSamples = 5;

    /**
     * 距离系数
     */
    public double distanceFactor = 48;

    /**
     * 时间系数
     */
    public double durationFactor = 1;

    /**
     * 采样队列
     */
    protected ArrayList<Sample> _samples = new ArrayList<Sample>();

    /**
     * 计算滚动
     *
     * @return
     */
    public Scroll computeScroll() {
        // 导出采样
        Sample end = this.popSample();
        Sample begin = this.computeBeginSample(end);
        // 结果容器
        Scroll result = new Scroll();
        result.radians = begin.radians;
        result.x = end.x;
        result.y = end.y;
        // 返回结果
        if (end.x == begin.x && end.y == begin.y) {
            return result;
        }
        double xSpace = end.x - begin.x;
        double ySpace = end.y - begin.y;
        double distance = Math.sqrt(xSpace * xSpace + ySpace * ySpace);
        double duration = end.timestamp - begin.timestamp;
        // 惯性结果
        double speed = distance / duration;
        result.distance = Math.pow(speed, speed / 2) * this.distanceFactor;
        result.duration = result.distance / speed * this.durationFactor;
        return result;
    }

    /**
     * 计算起点采样
     *
     * @param end
     * @return
     */
    protected Sample computeBeginSample(Sample end) {
        int x = end.x, y = end.y;
        Sample target = end;
        // 计算有效采样
        while (true) {
            Sample sample = this.popSample();
            if (sample == null) {
                break;
            }
            sample.radians = getRadians(sample.x, sample.y, x, y);
            if (target.radians > 0) {
                if (Math.abs(target.radians - sample.radians) > Math.PI * 0.5) { // 方向大于90度改变
                    break;
                }
            }
            // 导出采样
            target = sample;
        }
        return target;
    }

    /**
     * 添加采样
     *
     * @param x
     * @param y
     */
    public void addSample(int x, int y) {
        this._samples.remove(0);
        this._samples.add(new Sample(x, y));
    }

    /**
     * @param x
     * @param y
     */
    public void resetSample(int x, int y) {
        this._samples.clear();
        for (int i = 0; i < this.maxSamples; i++) {
            this._samples.add(new Sample(x, y));
        }
    }

    /**
     * 返回并移除最后测采样
     *
     * @return
     */
    protected Sample popSample() {
        int size = this._samples.size();
        if (size < 1) {
            return null;
        }
        return this._samples.remove(size - 1);
    }

    /**
     * 获得弧度
     *
     * @param centerX
     * @param centerY
     * @param targetX
     * @param targetY
     * @return
     */
    public static double getRadians(int centerX, int centerY, int targetX, int targetY) {
        if (centerX == targetX && centerY == targetY) {
            return 255;
        }
        int x = targetX - centerX;
        int y = targetY - centerY;
        double radius = Math.sqrt(x * x + y * y);
        double radians = Math.acos(x / radius);
        if (y < 0) {
            radians = 2 * Math.PI - radians;
        }
        return radians;
    }

    /**
     * 获得最短距离
     *
     * @param x
     * @param y
     * @param bound
     * @return
     */
    public static Point getShortestDistance(int x, int y, Rect bound) {
        int[] trbl = new int[]{
                y - bound.top,
                bound.right - x,
                bound.bottom - y,
                x - bound.left,
        };
        // 计算最短
        int k = 0;
        int v = trbl[0];
        for (int i = 1; i < 4; i++) {
            if (trbl[i] > v) {
                continue;
            }
            k = i;
            v = trbl[i];
        }
        // 设置最短位置
        Point target = new Point(x, y);
        switch (k) {
            case TOP:
                target.y = bound.top;
                break;
            case RIGHT:
                target.x = bound.right;
                break;
            case BOTTOM:
                target.y = bound.bottom;
                break;
            case LEFT:
                target.x = bound.left;
                break;
        }
        // 修正位置越界
        if (target.y < bound.top) {
            target.y = bound.top;
        }
        if (target.x > bound.right) {
            target.x = bound.right;
        }
        if (target.y > bound.bottom) {
            target.y = bound.bottom;
        }
        if (target.x < bound.left) {
            target.x = bound.left;
        }
        return target;
    }

    /**
     * 采样类型
     */
    public class Sample {
        /**
         * 时间
         */
        public final long timestamp;
        /**
         * 坐标
         */
        public final int x;
        /**
         * 坐标
         */
        public final int y;

        /**
         * 惯性弧度
         */
        public double radians = -1;

        /**
         * 默认构造
         */
        public Sample(int x, int y) {
            this.x = x;
            this.y = y;
            this.timestamp = new Date().getTime();
        }
        // End Struct
    }

    /**
     * 移动类型
     */
    public class Scroll {
        /**
         * 惯性终点坐标
         */
        public int x;
        /**
         * 惯性终点坐标
         */
        public int y;

        /**
         * 惯性弧度
         */
        public double radians;
        /**
         * 惯性距离
         */
        public double distance;
        /**
         * 移动持续时间
         */
        public double duration;

        /**
         * 获得边界裁切后的目标位置
         *
         * @param bound
         */
        public Point crop(Rect bound) {
            // 相对目标位置
            Point offset = new Point();
            offset.x = (int) (this.distance * Math.cos(this.radians));
            offset.y = (int) (this.distance * Math.sin(this.radians));
            // 绝对目标位置
            Point target = new Point();
            target.x = this.x + offset.x;
            target.y = this.y + offset.y;
            // 双向边界触点
            Point offsetV = new Point(offset);
            Point offsetH = new Point(offset);
            // Vertical
            if (target.y < bound.top) {
                int propTop = this.y - bound.top;
                offsetV.y = -propTop;
                offsetV.x = (int) (offsetV.y / Math.tan(this.radians));
            } else if (target.y > bound.bottom) {
                int propBottom = bound.bottom - this.y;
                offsetV.y = propBottom;
                offsetV.x = (int) (offsetV.y / Math.tan(this.radians));
            }
            // Horizontal
            if (target.x > bound.right) {
                int propRight = bound.right - this.x;
                offsetH.x = propRight;
                offsetH.y = (int) (offsetH.x * Math.tan(this.radians));
            } else if (target.x < bound.left) {
                int propLeft = this.x - bound.left;
                offsetH.x = -propLeft;
                offsetH.y = (int) (offsetH.x * Math.tan(this.radians));
            }
            // 计算最远边界
            double distanceV = Math.sqrt(offsetV.x * offsetV.x + offsetV.y * offsetV.y);
            double distanceH = Math.sqrt(offsetH.x * offsetH.x + offsetH.y * offsetH.y);
            // 最先碰触的边
            if (distanceV > distanceH) {
                target.x = this.x + offsetH.x;
                target.y = this.y + offsetH.y;
                this.duration = distanceH / this.distance * this.duration;//持续时间
                this.distance = distanceH;
            } else {
                target.x = this.x + offsetV.x;
                target.y = this.y + offsetV.y;
                this.duration = distanceV / this.distance * this.duration;//持续时间
                this.distance = distanceV;
            }
            return target;
        }

        /**
         * 获得目标位置
         *
         * @return
         */
        public Point getTarget() {
            Point target = new Point();
            target.x = this.x + (int) (this.distance * Math.cos(this.radians));
            target.y = this.y + (int) (this.distance * Math.sin(this.radians));
            return target;
        }
        // End Struct
    }

    // End Class
}
