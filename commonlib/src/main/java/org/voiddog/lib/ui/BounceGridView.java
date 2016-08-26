package org.voiddog.lib.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

import org.voiddog.lib.util.SizeUtil;

/**
 * 弹性grid view
 * Created by Dog on 2015/5/15.
 */
public class BounceGridView extends GridView {

    private static final int MAX_Y_OVERSCROLL_DISTANCE = 100;

    private Context mContext;
    private int mMaxYOverscrollDistance;

    public BounceGridView(Context context) {
        super(context);
        mContext = context;
        initBounceListView();
    }

    public BounceGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initBounceListView();
    }

    public BounceGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initBounceListView();
    }

    private void initBounceListView(){
        //get the density of the screen and do some maths with it on the max overscroll distance
        //variable so that you get similar behaviors no matter what the screen size
        final float density = SizeUtil.getLocalDisplayMetrics(mContext).density;

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
