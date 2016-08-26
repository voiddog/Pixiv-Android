package org.voiddog.lib.ui;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.TextView;

/**
 * A simple text label view that can be applied as a "badge" to any given {@link android.view.View}.
 * This class is intended to be instantiated at runtime rather than included in XML layouts.
 *
 * @author VoidDog
 */
public class BadgeView extends TextView implements ValueAnimator.AnimatorUpdateListener{
    private static final int DEFAULT_BADGE_COLOR = Color.RED;
    private static final int DEFAULT_TEXT_COLOR = Color.WHITE;

    GradientDrawable mBgDrawable = new GradientDrawable();
    Paint mTextPaint = new Paint();
    Rect mDrawRect = new Rect();
    ValueAnimator scaleAnimate = new ValueAnimator();
    Interpolator deceInter = new DecelerateInterpolator();
    Interpolator accInter = new AccelerateInterpolator();
    boolean isShow = false;

    public BadgeView(Context context) {
        super(context);
        init();
    }

    public BadgeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BadgeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void show(){
        if(isShow){
            return;
        }
        setVisibility(VISIBLE);
        isShow = true;
        scaleAnimate.setInterpolator(deceInter);
        scaleAnimate.start();
    }

    public void hide(){
        if(!isShow){
            return;
        }
        isShow = false;
        setVisibility(GONE);
        scaleAnimate.setInterpolator(accInter);
        scaleAnimate.start();
    }

    void init(){
        setBackgroundDrawable(null);
        scaleAnimate.setFloatValues(0.0f, 1.0f);
        scaleAnimate.setDuration(300);
        scaleAnimate.addUpdateListener(this);
        mBgDrawable.setColor(DEFAULT_BADGE_COLOR);
        setTextColor(DEFAULT_TEXT_COLOR);
        setTypeface(Typeface.DEFAULT_BOLD);

        updateUi();
    }

    void updateUi(){
        measurePadding();

        invalidate();
    }

    void measurePadding(){
        mTextPaint.setTextSize(getTextSize());

        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        float textHeight = fm.bottom - fm.top;
        float textWidth = mTextPaint.measureText(getText().toString());
        int paddingV, paddingH;

        paddingV = (int) (textHeight / 6);

        if(textHeight > textWidth){
            paddingH = (int) ((textHeight - textWidth) / 2) + paddingV;
        }
        else{
            paddingH = paddingV;
        }

        mBgDrawable.setCornerRadius(textHeight / 2 + paddingV);

        setPadding(paddingH, paddingV, paddingH, paddingV);
    }

    @Override
    public void setTextSize(float size) {
        super.setTextSize(size);
        updateUi();
    }

    @Override
    public void setTextSize(int unit, float size) {
        super.setTextSize(unit, size);
        updateUi();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measurePadding();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mDrawRect.set(0, 0, getMeasuredWidth(), getMeasuredHeight());
        setPivotX(getMeasuredWidth() >> 1);
        setPivotY(getMeasuredHeight() >> 1);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(getText().length() > 0) {
            mBgDrawable.setBounds(mDrawRect);
            mBgDrawable.draw(canvas);
        }
        super.onDraw(canvas);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        float value = (float) animation.getAnimatedValue();
        if(isShow){
            setAlpha(value);
            setScaleX(value);
            setScaleY(value);
        }
        else{
            setAlpha(1.0f - value);
            setScaleX(1.0f - value);
            setScaleY(1.0f - value);
        }
    }
}
