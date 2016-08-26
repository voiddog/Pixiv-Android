package org.voiddog.lib.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import org.voiddog.lib.R;

/**
 * 分割线
 * Created by Dog on 2015/7/14.
 */
public class DivideLineView extends ImageView{

    GradientDrawable mDrawableTopBottom, mDrawableBottomTop;

    public DivideLineView(Context context) {
        super(context);
        initRootView(null, 0);
    }

    public DivideLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initRootView(attrs, 0);
    }

    public DivideLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initRootView(attrs, defStyleAttr);
    }

    public void setOrientation(Orientation orientation){
        if(orientation == Orientation.BOTTOM){
            setBackgroundDrawable(mDrawableTopBottom);
        }
        else{
            setBackgroundDrawable(mDrawableBottomTop);
        }
    }

    void initRootView(AttributeSet attrs, int defStyleAttr){
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.DivideLineView, defStyleAttr, 0
        );

        int startColor = a.getColor(R.styleable.DivideLineView_divide_color, 0x55dedede);
        int color[] = {startColor, 0x00ffffff};


        mDrawableTopBottom = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, color);
        mDrawableTopBottom.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        mDrawableTopBottom.setShape(GradientDrawable.RECTANGLE);

        mDrawableBottomTop = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, color);
        mDrawableBottomTop.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        mDrawableBottomTop.setShape(GradientDrawable.RECTANGLE);

        int orientation = a.getInt(R.styleable.DivideLineView_divide_orientation, 1);

        if(orientation == 1){
            setBackgroundDrawable(mDrawableTopBottom);
        }
        else{
            setBackgroundDrawable(mDrawableBottomTop);
        }

        a.recycle();
    }

    public enum Orientation{
        TOP,
        BOTTOM
    }
}
