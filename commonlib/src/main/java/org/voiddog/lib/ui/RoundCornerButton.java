package org.voiddog.lib.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

import org.voiddog.lib.R;
import org.voiddog.lib.util.DrawCircleShapeUtil;


/**
 * 自定义圆角button 可以设置是否disable
 * Created by Dog on 2015/6/29.
 */
public class RoundCornerButton extends Button{
    /**
     * 是否disable
     */
    private boolean mIsDisable = true;
    /**
     * 背景
     */
    private GradientDrawable mBg;
    /**
     * 操作不可背景
     */
    private GradientDrawable mDisableBg;
    /**
     * 操作不可文字颜色
     */
    private int mDisableTextColor;
    /**
     * 可操作的时候的颜色
     */
    private int mEnableTextColor;
    /**
     * 边框色
     */
    private int mStrokeColor;
    /**
     * 边框粗细
     */
    private int mStrokeWidth;
    /**
     * 默认颜色
     */
    private int mDefaultColor;
    //波纹动画
    DrawCircleShapeUtil shapeUtil;

    public RoundCornerButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initRootView(attrs, 0);
    }

    public RoundCornerButton(Context context) {
        super(context);
        initRootView(null, 0);
    }

    public RoundCornerButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initRootView(attrs, defStyleAttr);
    }

    public void disableButton(){
        if(!mIsDisable) {
            mIsDisable = true;
            setTextColor(mDisableTextColor);
            setBackgroundDrawable(mDisableBg);
        }
    }

    public void enableButton(){
        if(mIsDisable) {
            mIsDisable = false;
            setTextColor(mEnableTextColor);
            setBackgroundDrawable(mBg);
        }
    }

    public boolean isDisable(){
        return mIsDisable;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(mIsDisable){
            return false;
        }
        super.onTouchEvent(event);
        return shapeUtil.onTouchEvent(event);
    }

    /**
     * 设置激活色
     */
    public void setActiveColor(int activeColor){
        shapeUtil.setPaintColor(activeColor);
    }

    /**
     * 设置默认背景色
     */
    public void setBg(int defaultColor){
        mDefaultColor = defaultColor;
        mBg.setColor(defaultColor);
    }

    /**
     * 设置非激活色
     */
    public void setDisableColor(int disableColor){
        mDisableBg.setColor(disableColor);
    }

    /**
     * 设置边框
     */
    public void setStroke(int width, int color){
        mStrokeWidth = width;
        mStrokeColor = color;
        mBg.setStroke(width, color);
        mDisableBg.setStroke(width, color);
    }

    /**
     * 文字的非激活色
     */
    public void setDisableTextColor(int textDisableColor){
        mDisableTextColor = textDisableColor;
    }

    /**
     * 初始化根视图
     */
    void initRootView(AttributeSet attrs, int defStyleAttr){

        mBg = new GradientDrawable();
        mDisableBg = new GradientDrawable();
        shapeUtil = new DrawCircleShapeUtil(this);

        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.RoundCornerButton, defStyleAttr, 0);

        mEnableTextColor = getCurrentTextColor();
        mDisableTextColor = a.getColor(R.styleable.RoundCornerButton_round_btn_disable_text_color, mEnableTextColor);
        mStrokeColor = a.getColor(R.styleable.RoundCornerButton_round_stroke_color, 0);
        mStrokeWidth = a.getDimensionPixelSize(R.styleable.RoundCornerButton_round_stroke_width, 0);

        mBg.setColor(mDefaultColor = a.getColor(R.styleable.RoundCornerButton_round_btn_bg, 0x00ffffff));
        shapeUtil.setPaintColor(a.getColor(R.styleable.RoundCornerButton_round_btn_active_bg, mDefaultColor));
        mDisableBg.setColor(a.getColor(R.styleable.RoundCornerButton_round_btn_disable_bg, mDefaultColor));

        int mRadioSize = a.getDimensionPixelSize(R.styleable.RoundCornerButton_round_btn_radio_size, 0);

        mBg.setShape(GradientDrawable.RECTANGLE);
        mBg.setCornerRadius(mRadioSize);

        mDisableBg.setShape(GradientDrawable.RECTANGLE);
        mDisableBg.setCornerRadius(mRadioSize);

        if(mStrokeWidth > 0){
            mBg.setStroke(mStrokeWidth, mStrokeColor);
            mDisableBg.setStroke(mStrokeWidth, mStrokeColor);
        }

        a.recycle();

        enableButton();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mBg.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
        mDisableBg.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        shapeUtil.onDraw(canvas);
    }
}
