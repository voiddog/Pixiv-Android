package org.voiddog.imageselector.ui.view.crop;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.GradientDrawable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ScrollerCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewParent;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import org.voiddog.lib.util.SizeUtil;
import org.voiddog.lib.util.ViewUtil;

/**
 * Created by qigengxin on 16/9/8.
 */
@CoordinatorLayout.DefaultBehavior(CropHeadLayout.HeadScrollingBehavior.class)
public class CropHeadLayout extends LinearLayout{
    /**
     * 拖动状态
     */
    static final int DRAG_STATE = 2;
    /**
     * 播放展开或者索隆状态
     */
    static final int ANIMATE_STATE = 4;

    /**
     * 底部的阴影
     */
    GradientDrawable shadow;
    /**
     * 底部的阴影高度 in dp
     */
    int shadowHeight = 5;

    public CropHeadLayout(Context context) {
        super(context);
        init(context, null);
    }

    public CropHeadLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    void init(Context context, AttributeSet attrs){
        int color[] = {0x77000000, 0x00ffffff};
        shadow = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, color);
        shadow.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        shadow.setShape(GradientDrawable.RECTANGLE);
        setWillNotDraw(false);
        shadowHeight = SizeUtil.dp2px(context, shadowHeight);

        // 这里修改 底部 padding 增加阴影高度
        setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom() + shadowHeight);
    }

    /**
     * 最后的触摸y坐标
     */
    float lastTouchY;
    /**
     * 当前状态 拖动 或者 播放动画状态
     */
    int state = 0;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_DOWN){
            lastTouchY = ev.getRawY();
        }
        return ev.getY() > (getHeight() - ViewCompat.getMinimumHeight(this));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 绘制阴影
        shadow.setBounds(0, getHeight() - shadowHeight, getWidth(), getHeight());
        shadow.draw(canvas);
    }

    /**
     * 获取底部阴影高度 in px
     * @return 返回px单位的阴影高度
     */
    public int getShadowHeight(){
        return shadowHeight;
    }

    /**
     * 速度检测
     */
    VelocityTracker velocityTracker;
    /**
     * 最小移动速度, 当大于这个速度的时候 播放动画
     */
    static final int MIN_VELOCITY = 1000;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(velocityTracker == null){
            velocityTracker = VelocityTracker.obtain();
        }
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            velocityTracker.clear();
        }

        // 拖动手势用 这里记录屏幕上的x 和 y 坐标给 velocity tracker
        MotionEvent screenEvent = MotionEvent.obtain(event);
        screenEvent.setLocation(event.getRawX(), event.getRawY());
        velocityTracker.addMovement(screenEvent);
        screenEvent.recycle();
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            lastTouchY = event.getRawY();
            velocityTracker.clear();
        }
        else if(event.getAction() == MotionEvent.ACTION_MOVE){
            int dy = (int) (event.getRawY() - lastTouchY);
            lastTouchY = event.getRawY();
            if(getParent() instanceof CoordinatorLayout){
                offsetView((CoordinatorLayout) getParent(), dy);
            }
        }
        else if(event.getAction() == MotionEvent.ACTION_UP
                || event.getAction() == MotionEvent.ACTION_CANCEL){
            velocityTracker.computeCurrentVelocity(1000);
            // 根据速度的阈值 播放相应的动画
            if(velocityTracker.getYVelocity() > MIN_VELOCITY){
                animateToBottom();
            }
            else if(velocityTracker.getYVelocity() < -MIN_VELOCITY){
                animateToTop();
            }
            else{
                animateAutoFit();
            }
        }

        return true;
    }

    /**
     * 移动视图, 不会超过边界
     * @param parent
     * @param dy > 0 down < 0 up
     * @return 消耗掉的移动数值
     */
    int offsetView(CoordinatorLayout parent, int dy){
        int ret;
        if(dy > 0){
            ret = Math.min(dy, -getOffset());
        }
        else{
            ret = Math.max(dy, -(getOffset() + getHeight() - ViewCompat.getMinimumHeight(this)));
        }
        ViewCompat.offsetTopAndBottom(this, ret);
        return ret;
    }

    /**
     * 播放收缩的动画
     */
    void animateToTop(){
        int offset = getOffset() + getHeight() - ViewCompat.getMinimumHeight(this);
        animateOffset(-offset);
    }

    /**
     * 播放展开的动画
     */
    void animateToBottom(){
        int offset = getOffset();
        animateOffset(-offset);
    }

    /**
     * 动画方式自适应
     */
    void animateAutoFit(){
        if(-getOffset() > getHeight() / 2){
            animateToTop();
        }
        else{
            animateToBottom();
        }
    }

    int tmpLastOffset = 0;
    ValueAnimator animator;

    /**
     * 通过动画方式移动相应的偏移量
     * @param offset > 0 向下 < 0 向上
     */
    void animateOffset(int offset){
        if(offset == 0){
            return;
        }
        if((state&ANIMATE_STATE) == ANIMATE_STATE){
            animator.cancel();
        }

        state |= ANIMATE_STATE;
        tmpLastOffset = 0;
        animator = new ValueAnimator();
        animator.setInterpolator(new DecelerateInterpolator(2.0f));
        animator.setIntValues(0, offset);
        animator.setDuration(500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                int moveOffset = value - tmpLastOffset;
                ViewCompat.offsetTopAndBottom(CropHeadLayout.this, moveOffset);
                tmpLastOffset = value;
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {
                state &= ~ANIMATE_STATE;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                state &= ~ANIMATE_STATE;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
        animator.start();
    }

    /**
     * 获取到头部相对于CoordinateLayout的偏移量
     * @return > 0 向下偏移 < 0 向上偏移
     */
    int getOffset(){
        return (int) getY();
    }

    /**
     * 头部的 behavior
     */
    public static class HeadScrollingBehavior extends CoordinatorLayout.Behavior<CropHeadLayout>{

        /**
         * 保存在coordinate layout 上触摸的事件 当前一次触摸用的是同一份,所以不需要担心儿子拦截后不能更新它的数值问题
         */
        MotionEvent lastTouchEvent;

        public HeadScrollingBehavior(){
            super();
        }

        public HeadScrollingBehavior(Context context, AttributeSet attributeSet){
            super(context, attributeSet);
        }

        @Override
        public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout
                , CropHeadLayout child, View directTargetChild, View target, int nestedScrollAxes) {
            // 始终捕获儿子的滚动事件
            return true;
        }

        @Override
        public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, CropHeadLayout child
                , View target, int dx, int dy, int[] consumed) {
            // 获取到 target 在 coordinate layout 下面的直接子view
            View targetChild = getChildView(coordinatorLayout, target);

            // 判断触摸点是够在 crop layout 中
            if(isTouchInHead(child)
                    || (child.state & DRAG_STATE) == DRAG_STATE){

                child.state |= DRAG_STATE;

            } else if (dy < 0 && !ViewCompat.canScrollVertically(target, -1)
                    && !canScrollDown(coordinatorLayout, targetChild, target)){
                // target 已经不能向下滚动 而且 target 也不能向下滚动的时候 crop layout 可以拉下来了
                child.state |= DRAG_STATE;

            }

            // 如果可以拖动 crop layout 消耗掉target的滑动
            if((child.state&DRAG_STATE) == DRAG_STATE) {
                consumed[1] = -child.offsetView(coordinatorLayout, -dy);
            }

            if(dy > 0 && consumed[1] != dy
                    && canScrollUp(coordinatorLayout, targetChild, target)){
                // 可以上滑
                consumed[1] = offsetScrollChildView(coordinatorLayout, targetChild, target, dy);
            }
            else if(dy < 0 && consumed[1] != dy
                    && canScrollDown(coordinatorLayout, targetChild, target)){
                // 可以下滑
                consumed[1] = offsetScrollChildView(coordinatorLayout, targetChild, target, dy);
            }
        }

        @Override
        public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, CropHeadLayout child, View target) {
            child.state &= ~DRAG_STATE;
            // 如果没有播放动画 触发自动适应动画
            if((child.state&ANIMATE_STATE) == 0){
                child.animateAutoFit();
            }
        }

        @Override
        public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, CropHeadLayout child
                , View target, float velocityX, float velocityY) {

            View targetChild = getChildView(coordinatorLayout, target);

            if((child.state&DRAG_STATE) == DRAG_STATE){
                if(velocityY > MIN_VELOCITY){
                    child.animateToTop();
                }
                else if(velocityY < -MIN_VELOCITY){
                    child.animateToBottom();
                }
                return true;
            }
            else if(velocityY > 0 && canScrollUp(
                    coordinatorLayout, targetChild, target)){
                // targetChild 可以滚动上去的时候 触发fling
                startScrollChildFling(coordinatorLayout, child, target, velocityY);
                return true;
            }
            else if(velocityY < 0 && canScrollDown(
                    coordinatorLayout, targetChild, target)){
                // targetChild 可以滚动下去的时候 触发fling
                startScrollChildFling(coordinatorLayout, child, target, velocityY);
                return true;
            }
            return false;
        }

        FlingRunnable flingRunnable;

        /**
         * targetChild开始fling
         * @param coordinatorLayout
         * @param child 消耗滚动事件的crop layout
         * @param target 触发滚动事件的view
         * @param velocityY 滚动的速度
         */
        void startScrollChildFling(CoordinatorLayout coordinatorLayout
                , CropHeadLayout child, View target, float velocityY){
            if(velocityY != 0) {
                flingRunnable = new FlingRunnable(
                        coordinatorLayout, getChildView(coordinatorLayout, target), (int) velocityY
                );
                ViewCompat.postOnAnimation(coordinatorLayout, flingRunnable);
            }
        }

        /**
         * targetChild fling 结束 把剩余的速度给target
         * @param coordinatorLayout
         * @param target 触发滚动事件的view
         * @param velocityY 剩余的速度
         */
        void onFlingFinished(CoordinatorLayout coordinatorLayout, View target, int velocityY){
            // no fling
            if(target instanceof RecyclerView){
                RecyclerView recyclerView = (RecyclerView) target;
                recyclerView.fling(0, velocityY);
            }
        }

        // 下面 scroll view 滑动 runnable
        class FlingRunnable implements Runnable{

            private CoordinatorLayout parent;
            private View child;
            private ChildScrollingBehavior behavior;
            ScrollerCompat scroller;
            int lastY = 0;
            boolean stopped = false;

            public FlingRunnable(CoordinatorLayout parent, View targetChild, int velocityY){
                behavior = (ChildScrollingBehavior)
                        ((CoordinatorLayout.LayoutParams)targetChild.getLayoutParams()).getBehavior();

                this.parent = parent;
                this.child = targetChild;
                scroller = ScrollerCompat.create(parent.getContext());
                if(behavior != null) {
                    scroller.fling(
                            0, 0,
                            0, velocityY,
                            0, 0,
                            behavior.getScrollOffsetY(), behavior.getOffsetY() + targetChild.getHeight() - parent.getHeight()
                    );
                }
            }

            public void stop(){
                stopped = true;
            }

            @Override
            public void run() {
                if(stopped){
                    flingRunnable = null;
                    return;
                }
                if (child != null && scroller != null) {
                    if (scroller.computeScrollOffset()) {
                        int offset = scroller.getCurrY() - lastY;
                        lastY = scroller.getCurrY();
                        behavior.offsetTopToBottom(child, -offset);
                        ViewCompat.postOnAnimation(parent, this);
                    }
                    else {
                        int leftVelocity = (int) (scroller.getFinalY() > 0 ?
                                scroller.getCurrVelocity() : -scroller.getCurrVelocity());
                        onFlingFinished(parent, child, leftVelocity);
                        flingRunnable = null;
                    }
                }
            }
        }

        @Override
        public boolean onNestedFling(CoordinatorLayout coordinatorLayout, CropHeadLayout child
                , View target, float velocityX, float velocityY, boolean consumed) {
            // 如果有滑动先停止滑动
            if(flingRunnable != null){
                flingRunnable.stop();
            }

            // fling 没有被消耗
            if(!consumed){
                if(velocityY > MIN_VELOCITY){
                    child.animateToTop();
                }
                else if(velocityY < -MIN_VELOCITY){
                    child.animateToBottom();
                }
                return true;
            }
            return false;
        }

        @Override
        public boolean onInterceptTouchEvent(CoordinatorLayout parent, CropHeadLayout child, MotionEvent ev) {
            lastTouchEvent = ev;
            return false;
        }

        /**
         * 移动 targetChild
         * @param parent coordinate Layout
         * @param targetChild 触发滚动事件的view 在 coordinate layout 下的直接子view
         * @param target 触发滚动事件的view
         * @param dy 滚动距离
         * @return
         */
        int offsetScrollChildView(CoordinatorLayout parent, View targetChild, View target, int dy){
            int ret = 0;

            ChildScrollingBehavior behavior = (ChildScrollingBehavior)
                    ((CoordinatorLayout.LayoutParams)targetChild.getLayoutParams()).getBehavior();
            if(behavior != null){
                if(dy > 0){
                    ret = Math.min(dy,
                            behavior.getOffsetY() + targetChild.getHeight() - parent.getHeight());
                }
                else{
                    ret = Math.max(dy, behavior.getScrollOffsetY());
                }
                behavior.offsetTopToBottom(targetChild, -ret);
            }
            return ret;
        }

        /**
         * 判断当前触摸点是否落在头部
         * @return
         */
        boolean isTouchInHead(CropHeadLayout view){
            float lastTopY = lastTouchEvent.getRawY();
            return lastTopY <= (ViewUtil.getRawY(view) + view.getHeight());
        }

        /**
         * 下面scroll view 可不可以上滑
         * @return
         */
        boolean canScrollUp(CoordinatorLayout parent, View child, View target){
            ChildScrollingBehavior behavior = (ChildScrollingBehavior)
                    ((CoordinatorLayout.LayoutParams)child.getLayoutParams()).getBehavior();
            if(behavior != null && behavior.getOffsetY() + child.getHeight() > parent.getHeight()){
                return true;
            }
            return false;
        }

        /**
         * 下面 scroll view 是否可以下滑
         * @return
         */
        boolean canScrollDown(CoordinatorLayout parent, View child, View target){
            ChildScrollingBehavior behavior = (ChildScrollingBehavior)
                    ((CoordinatorLayout.LayoutParams)child.getLayoutParams()).getBehavior();
            if(behavior != null && behavior.getScrollOffsetY() < 0){
                return true;
            }
            return false;
        }

        View getChildView(ViewParent parent, View target){
            View child = target;
            while(child.getParent() != null
                    && child.getParent() != parent){
                if(child.getParent() instanceof View){
                    child = (View) child.getParent();
                }
                else{
                    break;
                }
            }
            return child;
        }
    }

    /**
     * 底部滚动视图, 偏移距离为baseOffsetY + scrollOffsetY, 其中 baseOffsetY 是用于适应头部crop layout 的位置用的
     */
    public static class ChildScrollingBehavior extends CoordinatorLayout.Behavior{

        int baseOffsetY = 0;
        int scrollOffsetY = 0;

        public ChildScrollingBehavior(){
            super();
        }

        public ChildScrollingBehavior(Context context, AttributeSet attributeSet){
            super(context, attributeSet);
        }

        @Override
        public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
            return dependency instanceof CropHeadLayout;
        }

        @Override
        public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
            updateChildOffset(child);
            return true;
        }

        @Override
        public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
            baseOffsetY = (int) (dependency.getY() + dependency.getHeight()
                    - ((CropHeadLayout)dependency).getShadowHeight());
            updateChildOffset(child);
            return true;
        }

        public int getOffsetY(){
            return baseOffsetY + scrollOffsetY;
        }

        public int getScrollOffsetY(){
            return scrollOffsetY;
        }

        public void offsetTopToBottom(View child, int offset){
            this.scrollOffsetY += offset;
            updateChildOffset(child);
        }

        /**
         * 保证在head view以下 并且 底部位置不会低于parent
         * @param child
         */
        void updateChildOffset(View child){
            int offsetY = baseOffsetY + scrollOffsetY;
            offsetY = Math.max(offsetY, 0);
            scrollOffsetY = offsetY - baseOffsetY;
            child.layout(0, offsetY,
                    child.getMeasuredWidth(), child.getMeasuredHeight() + offsetY);
        }
    }
}
