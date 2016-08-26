package org.voiddog.lib.util;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

/**
 * 绘制波纹
 * Created by Dog on 2015/7/22.
 */
public class DrawCircleShapeUtil{
    //点击drawable
    ShapeDrawable mClickDrawable;
    //消失动画
    ValueAnimator mStartValueAnimation, mEndValueAnimation;
    //绘制区域
    Rect mDrawRect;
    //点击的点
    int startX, startY;
    //扩展区域
    final int initSize, finalSize;
    //开始扩展大小
    int startSize;
    //上下文
    Context mContext;
    //绑定的view
    View mView;
    //画笔颜色
    int mPaintColor = Color.rgb(200, 200, 200);
    //最大透明度
    int mBaseAlpha = 80;

    public DrawCircleShapeUtil(View view){
        mContext = view.getContext();
        initSize = SizeUtil.dp2px(view.getContext(), 30);
        finalSize = SizeUtil.getScreenWidth(view.getContext()) << 1;
        mView = view;

        mClickDrawable = new ShapeDrawable(new OvalShape());
        setPaintColor(mPaintColor);
        mDrawRect = new Rect();

        mStartValueAnimation = new ValueAnimator();
        mStartValueAnimation.setInterpolator(new DecelerateInterpolator());
        mStartValueAnimation.setDuration(3000);
        mStartValueAnimation.setFloatValues(1.0f, 0.0f);
        mStartValueAnimation.addUpdateListener(new StartAnimation());

        mEndValueAnimation = new ValueAnimator();
        mEndValueAnimation.setInterpolator(new DecelerateInterpolator());
        mEndValueAnimation.setDuration(300);
        mEndValueAnimation.setFloatValues(1.0f, 0.0f);
        mEndValueAnimation.addUpdateListener(new EndAnimation());

        if(view instanceof ViewGroup){
            view.setWillNotDraw(false);
        }
    }

    public void setPaintColor(int color){
        mPaintColor = color & 0x00ffffff;
        mClickDrawable.getPaint().setColor(mPaintColor + (mBaseAlpha << 24));
    }

    /**
     * 触摸事件
     * @return 是否消耗
     */
    public boolean onTouchEvent(MotionEvent event){
        startSize = Math.max(initSize,
                mView.getMeasuredHeight() > mView.getMeasuredWidth() ? mView.getMeasuredWidth() : mView.getMeasuredHeight()
        );
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:{
                startX = (int) event.getX();
                startY = (int) event.getY();
                mClickDrawable.getPaint().setColor(mPaintColor + (mBaseAlpha << 24));

                mEndValueAnimation.cancel();
                mStartValueAnimation.setDuration(3000);
                mStartValueAnimation.start();
                break;
            }
            case MotionEvent.ACTION_MOVE:{
                startX = (int) event.getX();
                startX = startX < 0 ? 0 : startX;
                startX = startX > mView.getMeasuredWidth() ? mView.getMeasuredWidth() : startX;
                startY = (int) event.getY();
                startY = startY < 0 ? 0 : startY;
                startY = startY > mView.getMeasuredHeight() ? mView.getMeasuredHeight() : startY;
                break;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:{
                int minSize = Math.max(mView.getMeasuredWidth(), mView.getMeasuredHeight()) * 3 / 4;
                if(startSize < minSize){
                    startSize = minSize;
                }
                mStartValueAnimation.setCurrentPlayTime(
                        mStartValueAnimation.getCurrentPlayTime() / 10
                );
                mStartValueAnimation.setDuration(300);
                mEndValueAnimation.start();
            }
        }
        return true;
    }

    /**
     * 在View绘制的时候调用
     * @param canvas 绘制
     */
    public void onDraw(Canvas canvas){
        mClickDrawable.setBounds(mDrawRect);
        mClickDrawable.draw(canvas);
    }

    class StartAnimation implements ValueAnimator.AnimatorUpdateListener{

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            float v = (float) animation.getAnimatedValue();
            int size = (int) (finalSize + v*(startSize - finalSize)) >> 1;
            if(size > 0) {
                mDrawRect.set(startX - size, startY - size, startX + size, startY + size);
                mView.invalidate();
            }
        }
    }

    class EndAnimation implements ValueAnimator.AnimatorUpdateListener{

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            float v = (float) animation.getAnimatedValue();
            int alpha = (int) (v*mBaseAlpha);
            mClickDrawable.getPaint().setColor(mPaintColor + (alpha << 24));
            mView.invalidate();
        }
    }
}
