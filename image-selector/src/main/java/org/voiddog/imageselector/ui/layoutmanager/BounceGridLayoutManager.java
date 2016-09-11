package org.voiddog.imageselector.ui.layoutmanager;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * 文件描述
 *
 * @author qigengxin
 * @since 2016-09-10 19:52
 */

public class BounceGridLayoutManager extends GridLayoutManager implements ValueAnimator.AnimatorUpdateListener{

    RecyclerView mRecycleView = null;
    OverScrollItemTouchListener mOverScrollListener = null;

    public BounceGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public BounceGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public BounceGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    @Override
    public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);
        mRecycleView = view;
        mRecycleView.addOnItemTouchListener(mOverScrollListener = new OverScrollItemTouchListener());
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int ret = super.scrollVerticallyBy(dy, recycler, state);

        int totalOverScroll = getOverScroll();
        int overScroll = dy - ret;

        float k = 4.0f * totalOverScroll / getHeight();

        overScroll -= Math.abs(overScroll) * k;
        if(Math.abs(overScroll) < dp2px(mRecycleView.getContext() ,2)){
            overScroll = 0;
        }
        offsetChildrenVertical(-overScroll);

        int res = dy;
        if(totalOverScroll < 0 && overScroll == 0 && dy < 0){
            res = 0;
        }
        else if(totalOverScroll > 0 && overScroll == 0 && dy > 0){
            res = 0;
        }

        if(res == 0 && mOverScrollListener.getLastAction() == MotionEvent.ACTION_UP){
            onOverScroll();
        }

        return res;
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        return super.scrollHorizontallyBy(dx, recycler, state);
    }

    private int getOverScroll(){
        if(getChildCount() <= 0){
            return 0;
        }

        View topView = getChildAt(0);
        if(topView.getTop() > 0){
            return -topView.getTop();
        }

        View lastView = getChildAt(getChildCount() - 1);
        int height = Math.min(getHeight(), lastView.getBottom() - topView.getTop());
        if(getPosition(lastView) != getItemCount() - 1){
            return 0;
        }
        else if(height > lastView.getBottom()){
            return height - lastView.getBottom();
        }
        return 0;
    }

    ValueAnimator mAnimator = null;

    private void onOverScroll(){
        if(mAnimator == null){
            mAnimator = new ValueAnimator();
            mAnimator.addUpdateListener(this);
            mAnimator.setDuration(2000);
            mAnimator.setInterpolator(new DecelerateInterpolator(2.0f));
            mAnimator.setFloatValues(1, 0);
        }
        mAnimator.cancel();
        mAnimator.start();
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        if(mRecycleView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE){
            return;
        }

        float v = (float) animation.getAnimatedValue();
        int dv = (int) (getOverScroll()*(1.0 - v));
        offsetChildrenVertical(dv);
    }

    class OverScrollItemTouchListener implements RecyclerView.OnItemTouchListener {

        int mLastAction = 0;

        public int getLastAction(){
            return mLastAction;
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            mLastAction = e.getAction();
            if(mLastAction == MotionEvent.ACTION_UP){
                if(getOverScroll() != 0){
                    onOverScroll();
                }
            }
            else if(mLastAction == MotionEvent.ACTION_DOWN){
                if(mAnimator != null){
                    mAnimator.cancel();
                }
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}
    }

    public static int dp2px(Context context, int dp) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int px = (int)((double)((float)dp * dm.density) + 0.5D);
        return px;
    }

}
