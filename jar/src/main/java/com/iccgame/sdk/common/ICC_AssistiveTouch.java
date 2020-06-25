package com.iccgame.sdk.common;

import android.animation.Animator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Base64;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by Administrator on 2015/10/19.
 */
public class ICC_AssistiveTouch extends FrameLayout {

    /**
     * 默认设置
     */
    protected WindowManager.LayoutParams _layoutParams;

    /**
     * 显示图像
     */
    protected ImageView _imageView;

    /**
     * 动画句柄
     */
    protected ValueAnimator _valueAnimator;

    /**
     * 触摸结构监听
     */
    protected ICC_AssistiveTouchListener _assistiveTouchListener = new ICC_AssistiveTouchListener();

    /**
     * 触摸处理事件
     */
    protected TouchMoveListener _touchMoveListener = new TouchMoveListener(this);

    /**
     * 移动范围
     */
    public Rect stopBound = new Rect();

    /**
     *
     */
    public Rect dragBound = new Rect();

    /**
     * 构造函数
     *
     * @param context
     */
    public ICC_AssistiveTouch(Activity context) {
        // 初始对象
        super(context);
        // 绑定操作
        this.setOnTouchListener(this._touchMoveListener);
    }

    /**
     * 构造函数
     *
     * @param context
     * @param pngBase64
     */
    public ICC_AssistiveTouch(Activity context, String pngBase64) {
        // 初始对象
        super(context);
        // 设置样式
        this.setImage(pngBase64);
    }

    /**
     * 构造函数
     *
     * @param context
     * @param pngBase64
     * @param position
     */
    public ICC_AssistiveTouch(Activity context, String pngBase64, Point position) {
        // 初始对象
        this(context);
        // 设置样式
        this.setImage(pngBase64, position);
    }

    /**
     * 设置图像
     *
     * @param pngBase64
     */
    public synchronized void setImage(String pngBase64) {
        ICC_Log.info(String.format("ICC_AssistiveTouch.setImage(String(%d))", pngBase64.length()));
        try {
            byte[] bytes = Base64.decode(pngBase64, Base64.DEFAULT);
            Bitmap image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            if (this._imageView == null) {
                this._imageView = new ImageView(this.getContext());
                this.addView(this._imageView);
            }
            // 改变内容
            this._imageView.setImageBitmap(image);
            this._imageView.setMaxWidth(image.getWidth());
            this._imageView.setMaxHeight(image.getHeight());
            // 更新尺寸
            WindowManager.LayoutParams layoutParams = this.getWindowLayoutParams();
            layoutParams.width = image.getWidth();
            layoutParams.height = image.getHeight();
            // 显示范围
            this.stopBound = this.getBound(image.getWidth(), image.getHeight());
            // 拖动范围
            this.dragBound = this.padBound(this.stopBound, image.getWidth(), image.getHeight());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更换图片
     *
     * @param pngBase64
     */
    public synchronized void setImage(String pngBase64, Point position) {
        if (position == null) {
            ICC_Log.debug("ICC_AssistiveTouch.setImage(String, null)");
        } else {
            ICC_Log.debug(String.format("ICC_AssistiveTouch.setImage(String, Point(x:%d, y:%d))", position.x, position.y));
        }
        try {
            // 更换图片
            this.setImage(pngBase64);
            // 更新位置
            WindowManager.LayoutParams layoutParams = this.getWindowLayoutParams();
            if (position == null) {
                layoutParams.x = this.stopBound.right;
                layoutParams.y = this.stopBound.bottom / 2;
            } else {
                layoutParams.x = position.x;
                layoutParams.y = position.y;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得屏内范围
     *
     * @param width
     * @param height
     * @return
     */
    public Rect getBound(int width, int height) {
        Rect bound = new Rect();
        bound.top = this.isFullScreen() ? 0 : this.getStatusBarHeight();
        bound.right = this.getDisplayWidth() - width;
        bound.bottom = this.getDisplayHeight() - height;// - this.getStatusBarHeight();
        bound.left = 0;
        ICC_Log.debug(String.format("ICC_AssistiveTouch.getBound(int(%d), int(%d)) return Rect(t:%d, r:%d, b:%d, l:%d)", width, height, bound.top, bound.right, bound.bottom, bound.left));
        return bound;
    }

    /**
     * 填充扩边
     *
     * @param bound
     * @param width
     * @param height
     * @return
     */
    public Rect padBound(Rect bound, int width, int height) {
        Rect outside = new Rect();
        outside.top = bound.top - (int) (height * 0.382);
        outside.right = bound.right + (int) (width * 0.382);
        outside.bottom = bound.bottom + (int) (height * 0.382);
        outside.left = bound.left - (int) (width * 0.382);
        ICC_Log.debug(String.format("ICC_AssistiveTouch.padBound(Rect, int(%d), int(%d)) return Rect(t:%d, r:%d, b:%d, l:%d)", width, height, outside.top, outside.right, outside.bottom, outside.left));
        return outside;
    }

    /**
     * 设置监听处理
     *
     * @param listener
     */
    public synchronized void setTouchListener(ICC_AssistiveTouchListener listener) {
        ICC_Log.debug("ICC_AssistiveTouch.setTouchListener(ICC_AssistiveTouchListener)");
        this._assistiveTouchListener = listener;
    }

    /**
     * 播放动画
     *
     * @param animation
     */
    public synchronized void startValueAnimation(ValueAnimator animation) {
        ICC_Log.debug("ICC_AssistiveTouch.startValueAnimation(ValueAnimator)");
        // 清理动画
        this.clearValueAnimation();
        // 播放动画
        this._valueAnimator = animation;
        this._valueAnimator.start();
    }

    /**
     * 清理动画
     */
    protected synchronized void clearValueAnimation() {
        ICC_Log.debug("ICC_AssistiveTouch.clearValueAnimation()");
        if (this._valueAnimator == null) {
            return;
        }
        if (this._valueAnimator.isRunning()) {
            this._valueAnimator.cancel();
        }
        this._valueAnimator = null;
    }

    /**
     * 获得显示区像
     *
     * @return
     */
    public synchronized Drawable getImageDrawable() {
        return this._imageView.getDrawable();
    }

    /**
     * 获得图像宽度
     *
     * @return
     */
    public int getImageWidth() {
        return this.getImageDrawable().getIntrinsicWidth();
    }

    /**
     * 获得图像高度
     *
     * @return
     */
    public int getImageHeight() {
        return this.getImageDrawable().getIntrinsicHeight();
    }

    /**
     * 获得显示区宽度
     *
     * @return
     */
    public int getDisplayWidth() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2) {
            return ((Activity) this.getContext()).getWindowManager().getDefaultDisplay().getWidth();
        }
        Rect displayRect = new Rect();
        ((Activity) this.getContext()).getWindowManager().getDefaultDisplay().getRectSize(displayRect);
        return displayRect.width();
    }

    /**
     * 获得图像高度
     *
     * @return
     */
    public int getDisplayHeight() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2) {
            return ((Activity) this.getContext()).getWindowManager().getDefaultDisplay().getHeight();
        }
        Rect displayRect = new Rect();
        ((Activity) this.getContext()).getWindowManager().getDefaultDisplay().getRectSize(displayRect);
        return displayRect.height();
    }

    /**
     * 获得状态栏高度
     *
     * @return
     */
    public int getStatusBarHeight() {
        return (int) Math.ceil(25 * this.getContext().getResources().getDisplayMetrics().density);
    }

    /**
     * 是否全屏显示
     *
     * @return
     */
    public boolean isFullScreen() {
        WindowManager.LayoutParams attrs = ((Activity) getContext()).getWindow().getAttributes();
        return (attrs.flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) > 0;
    }

    /**
     * 获得注入的窗体句柄
     *
     * @return
     */
    public WindowManager getWindowManager() {
        return (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
    }

    /**
     * 获得窗体的设置
     *
     * @return
     */
    public WindowManager.LayoutParams getWindowLayoutParams() {
        if (this._layoutParams == null) {
            // 窗体的布局样式
            this._layoutParams = new WindowManager.LayoutParams();
            //this._layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE; // 类似电话通知,优先级较高
            //this._layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_DIALOG ; // 类似系统通知,优先级最高,需要单开权限
            this._layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_SUB_PANEL;
            this._layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE // 不能输入
                    + WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN // 全屏效果
                    + WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS; // 允许出屏
            // 设置显示的模式
            this._layoutParams.format = PixelFormat.TRANSPARENT;
            // 设置对齐的方法
            this._layoutParams.gravity = Gravity.LEFT + Gravity.TOP;
        }
        return this._layoutParams;
    }

    /**
     * 显示浮标
     *
     * @return
     */
    public synchronized boolean show() {
        ICC_Log.info("ICC_AssistiveTouch.show()");
        if (this.getParent() != null) {
            ICC_Log.debug("ICC_AssistiveTouch.getParent() is not null");
            return false;
        }
        WindowManager.LayoutParams layoutParams = getWindowLayoutParams();
        this.getWindowManager().addView(this, layoutParams);
        this.fadeOut();
        ICC_Log.debug(String.format("ICC_AssistiveTouch.show(), x:%d, y:%d", layoutParams.x, layoutParams.y));
        return true;
    }

    /**
     * 隐藏浮标
     *
     * @return
     */
    public synchronized boolean hide() {
        ICC_Log.info("ICC_AssistiveTouch.hide()");
        if (this.getParent() == null) {
            ICC_Log.debug("ICC_AssistiveTouch.getParent() is null");
            return false;
        }
        // 清理动画
        this.clearValueAnimation();
        this.clearAnimation();
        // 移除显示
        this.getWindowManager().removeView(this);
        ICC_Log.debug("ICC_AssistiveTouch.hide()");
        return true;
    }

    /**
     * 淡入浮标
     */
    public synchronized void fadeIn() {
        ICC_Log.debug("ICC_AssistiveTouch.fadeIn()");
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return;
        }
        Animation animation = new AlphaAnimation(Animation.RELATIVE_TO_SELF, 1f);
        animation.setDuration(200);
        animation.setFillAfter(true);
        // 运行动画
        this._imageView.clearAnimation();
        this._imageView.startAnimation(animation);
    }

    /**
     * 淡出浮标
     */
    public synchronized void fadeOut() {
        ICC_Log.debug("ICC_AssistiveTouch.fadeOut()");
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return;
        }
        // 贴边动画
        dragPropsStop();
        // 淡化动画
        Animation animation = new AlphaAnimation(Animation.RELATIVE_TO_SELF, 0.382f);
        animation.setDuration(400);
        animation.setStartOffset(4000);
        animation.setFillAfter(true);
        // 运行动画
        this._imageView.clearAnimation();
        this._imageView.startAnimation(animation);
    }


    /**
     * 拖动惯性效果
     */
    protected synchronized void dragThrowProps(ICC_VelocityTracker.Scroll scroll) {
        ICC_Log.debug("ICC_AssistiveTouch.dragThrowProps(ICC_VelocityTracker.Scroll)");
        if (scroll.radians < 0) {
            fadeOut();
            return;
        }
        // 修正越界
        Point target = scroll.crop(this.dragBound);
        // 动画变更属性
        PropertyValuesHolder[] propertys = new PropertyValuesHolder[2];
        propertys[0] = PropertyValuesHolder.ofInt("x", scroll.x, target.x);
        propertys[1] = PropertyValuesHolder.ofInt("y", scroll.y, target.y);
        // 设置参数
        DragThrowPropsUpdateListener listener = new DragThrowPropsUpdateListener(this);
        // 创建动画
        ValueAnimator animation = new ValueAnimator();
        animation.setValues(propertys[0], propertys[1]);
        animation.setDuration((int) scroll.duration);
        animation.addUpdateListener(listener);
        animation.addListener(listener);
        this.startValueAnimation(animation);
        // 打印日志
        ICC_Log.debug(String.format("dragThrowProps - radians:%.4f, distance:%.2f, duration:%.2f", scroll.radians, scroll.distance, scroll.duration));
        ICC_Log.debug(String.format("dragThrowProps - startX:%d, startY:%d, targetX:%d, targetY:%d", scroll.x, scroll.y, target.x, target.y));
    }

    /**
     * 回弹浮标
     */
    protected synchronized void dragPropsStop() {
        ICC_Log.debug("ICC_AssistiveTouch.dragPropsStop()");
        // 缓存参数
        WindowManager.LayoutParams layoutParams = this.getWindowLayoutParams();
        int x = layoutParams.x;
        int y = layoutParams.y;
        // 统计距离
        Point target = ICC_VelocityTracker.getShortestDistance(x, y, this.stopBound);
        // 动画变更属性
        PropertyValuesHolder[] propertys = new PropertyValuesHolder[2];
        propertys[0] = PropertyValuesHolder.ofInt("x", x, target.x);
        propertys[1] = PropertyValuesHolder.ofInt("y", y, target.y);
        // 设置参数
        MoveUpdateListener listener = new MoveUpdateListener(this);
        // 计算动画时长
        double duration;
        if (x < this.stopBound.left
                || x > this.stopBound.right
                || y < this.stopBound.top
                || y > this.stopBound.bottom) {
            duration = 150;
        } else {
            // 越界延长
            int dx = target.x - x;
            int dy = target.y - y;
            double distance = Math.sqrt(dx * dx + dy * dy);
            // 计算时间
            double length = Math.min(this.getDisplayHeight(), this.getDisplayWidth()) / 2;
            double offset = Math.cos(distance / length * Math.PI / 2) * length / 2;
            duration = (distance + offset) / (length / 150);
        }
        // 创建动画
        ValueAnimator animator = new ValueAnimator();
        animator.setTarget(layoutParams);
        animator.setValues(propertys[0], propertys[1]);
        animator.setDuration((int) duration);
        //animator.setInterpolator(new AccelerateInterpolator());
        animator.addUpdateListener(listener);
        this.startValueAnimation(animator);
        // 打印日志
        ICC_Log.debug(String.format("dragPropsStop - startX:%d, startY:%d, targetX:%d, targetY:%d", x, y, target.x, target.y));
        // 保存位置
        _assistiveTouchListener.onMoved(ICC_AssistiveTouch.this, target);
    }

    /**
     * 惯性动画刷新
     */
    protected class DragThrowPropsUpdateListener extends MoveUpdateListener implements ValueAnimator.AnimatorListener {

        /**
         * 构造函数
         *
         * @param context
         */
        public DragThrowPropsUpdateListener(ICC_AssistiveTouch context) {
            super(context);
        }

        @Override
        public void onAnimationStart(Animator animation) {
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            fadeOut();
        }

        @Override
        public void onAnimationCancel(Animator animation) {
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }
        // End Sub Class
    }

    /**
     * 移动动画刷新
     */
    protected class MoveUpdateListener implements ValueAnimator.AnimatorUpdateListener {

        /**
         * 上下文
         */
        public final ICC_AssistiveTouch context;

        /**
         * 缓存窗口参数指针
         */
        public final WindowManager.LayoutParams layoutParams;

        /**
         * 缓存管理器指针
         */
        public final WindowManager windowManager;

        /**
         * 构造函数
         *
         * @param context
         */
        public MoveUpdateListener(ICC_AssistiveTouch context) {
            this.context = context;
            this.windowManager = context.getWindowManager();
            this.layoutParams = context.getWindowLayoutParams();
        }

        /**
         * 刷新显示
         *
         * @param animator
         */
        @Override
        public void onAnimationUpdate(ValueAnimator animator) {
            try {
                this.layoutParams.x = (Integer) animator.getAnimatedValue("x");
                this.layoutParams.y = (Integer) animator.getAnimatedValue("y");
                this.windowManager.updateViewLayout(this.context, this.layoutParams);
            } catch (Exception e) {
                animator.cancel();
                e.printStackTrace();
            }
        }

        // End Sub Class
    }

    /**
     * 处理触摸事件 内部类
     */
    protected class TouchMoveListener implements View.OnTouchListener {

        /**
         * 当前监听所属指针
         */
        public final ICC_AssistiveTouch context;

        /**
         * 拖动剧中偏移
         */
        protected float offsetX, offsetY;

        /**
         * 拖动起点位置
         */
        protected int startX, startY;

        /**
         * 是否拖动
         */
        protected boolean isMoved = false;

        /**
         * 移动的阀值
         */
        protected int threshold = 0;

        /**
         * 惯性计算
         */
        public final ICC_VelocityTracker velocityTracker;

        /**
         * 缓存窗口参数指针
         */
        public final WindowManager.LayoutParams layoutParams;

        /**
         * 缓存管理器指针
         */
        public final WindowManager windowManager;


        /**
         * 构造函数
         *
         * @param context
         */
        public TouchMoveListener(ICC_AssistiveTouch context) {
            ICC_Log.debug("ICC_AssistiveTouch bind TouchMoveListener");
            // 缓存参数
            this.context = context;
            this.windowManager = context.getWindowManager();
            this.layoutParams = context.getWindowLayoutParams();
            // 动画效果
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
                this.velocityTracker = new ICC_VelocityTracker();
            } else {
                this.velocityTracker = null;
            }
        }

        /**
         * 触摸事件
         *
         * @param view
         * @param event
         * @return
         */
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // 计算居中偏移
                    this.offsetX = event.getX();
                    this.offsetY = event.getY(); //  + this.context.getStatusBarHeight(); // 是否增加偏移？与属性 FLAG_LAYOUT_IN_SCREEN 有密切关系
                    // 设置起点
                    this.isMoved = false;
                    this.startX = this.layoutParams.x;
                    this.startY = this.layoutParams.y;
                    // 最小移动乏值
                    this.threshold = (int) (Math.min(view.getWidth(), view.getHeight()) * 0.25f);
                    //
                    ICC_Log.debug(String.format("ICC_AssistiveTouch drag start, x:%d, y:%d, offsetX:%.2f, offsetY:%.2f", this.startX, this.startY, this.offsetX, this.offsetY));
                    // 惯性处理
                    if (this.velocityTracker == null) {
                        break;
                    }
                    // 停止动画
                    this.context.clearValueAnimation();
                    // 播放动画
                    this.context.fadeIn();
                    // 采样惯性
                    this.velocityTracker.distanceFactor = Math.min(view.getWidth(), view.getHeight()) * 0.000618;
                    this.velocityTracker.resetSample(this.layoutParams.x, this.layoutParams.y);
                    break;
                case MotionEvent.ACTION_UP:
                    ICC_Log.debug(String.format("ICC_AssistiveTouch drag stop, x:%d, y:%d", this.layoutParams.x, this.layoutParams.y));
                    // 位置移动
                    if (this.isMoved == false) {
                        ICC_Log.debug("ICC_AssistiveTouch touch");
                        _assistiveTouchListener.onTouch(ICC_AssistiveTouch.this);
                    }
                    // 惯性处理
                    if (this.velocityTracker == null) {
                        _assistiveTouchListener.onMoved(ICC_AssistiveTouch.this, new Point(this.layoutParams.x, this.layoutParams.y));
                        break;
                    }
                    this.context.dragThrowProps(this.velocityTracker.computeScroll());
                    break; // 继续下去
                case MotionEvent.ACTION_MOVE:
                    this.layoutParams.x = (int) (event.getRawX() - this.offsetX);
                    this.layoutParams.y = (int) (event.getRawY() - this.offsetY);
                    // 修正触屏
                    if (this.layoutParams.y < dragBound.top) {
                        this.layoutParams.y = dragBound.top;
                    } else if (this.layoutParams.y > dragBound.bottom) {
                        this.layoutParams.y = dragBound.bottom;
                    }
                    if (this.layoutParams.x > dragBound.right) {
                        this.layoutParams.x = dragBound.right;
                    } else if (this.layoutParams.x < dragBound.left) {
                        this.layoutParams.x = dragBound.left;
                    }
                    // 刷新显示
                    this.windowManager.updateViewLayout(this.context, this.layoutParams);
                    // 忽略微动
                    if (this.isMoved == false) {
                        if (Math.max(Math.abs(this.layoutParams.x - this.startX), Math.abs(this.layoutParams.y - this.startY)) > this.threshold) {
                            this.isMoved = true;
                        }
                    }
                    // 惯性处理
                    if (this.velocityTracker == null) {
                        break;
                    }
                    this.velocityTracker.addSample(this.layoutParams.x, this.layoutParams.y);
                    break;
            }
            return false;
        }
        // End Sub Class
    }

// End Class
}
