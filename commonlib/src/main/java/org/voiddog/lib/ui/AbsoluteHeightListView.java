package org.voiddog.lib.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 绝对长度的list view
 * Created by voiddog on 2015/10/17.
 */
public class AbsoluteHeightListView extends ListView{

    public AbsoluteHeightListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AbsoluteHeightListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AbsoluteHeightListView(Context context) {
        super(context);
    }

    /**
     * 重写该方法，达到使ListView适应ScrollView的效果
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
