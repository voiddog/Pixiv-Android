package org.voiddog.lib.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import org.voiddog.lib.util.SizeUtil;

/**
 * 弹性scroll view
 * Created by Dog on 2015/5/28.
 */
public class BounceScrollView extends ScrollView{

    private static final int MAX_Y_OVERSCROLL_DISTANCE = 100;

    private int mMaxYOverscrollDistance;

    public BounceScrollView(Context context) {
        super(context);
        initBounceScrollView();
    }

    public BounceScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initBounceScrollView();
    }

    public BounceScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initBounceScrollView();
    }

    private void initBounceScrollView(){
        //get the density of the screen and do some maths with it on the max overscroll distance
        //variable so that you get similar behaviors no matter what the screen size
        final float density = SizeUtil.getLocalDisplayMetrics(getContext()).density;

        mMaxYOverscrollDistance = (int) (density * MAX_Y_OVERSCROLL_DISTANCE);
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX,
                                   int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent){
        //This is where the magic happens, we have replaced the incoming maxOverScrollY with our own custom variable mMaxYOverscrollDistance;
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY,
                maxOverScrollX, mMaxYOverscrollDistance, isTouchEvent);
    }
}
