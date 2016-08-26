package org.voiddog.lib.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

import org.voiddog.lib.util.SizeUtil;

/**
 * Created by Administrator on 2015/7/2 0002.
 */
public class BounceExpandableListView extends ExpandableListView{
    private static final int MAX_Y_OVERSCROLL_DISTANCE = 100;
    private Context mContext;
    private int mMaxYOverscrollDistance;

    public BounceExpandableListView(Context context) {
        super(context);
        mContext = context;
        initBounceListView();
    }

    public BounceExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initBounceListView();
    }

    public BounceExpandableListView(Context context, AttributeSet attrs, int defStyleAttr) {
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
