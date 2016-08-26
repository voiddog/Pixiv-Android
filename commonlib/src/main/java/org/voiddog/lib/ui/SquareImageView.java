package org.voiddog.lib.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 方形的image view
 * Created by Dog on 2015/7/7.
 */
public class SquareImageView extends ImageView{
    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SquareImageView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }
}
