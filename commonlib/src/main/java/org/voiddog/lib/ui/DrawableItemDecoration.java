package org.voiddog.lib.ui;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * Created by qigengxin on 16/8/29.
 */
public class DrawableItemDecoration extends RecyclerView.ItemDecoration{
    public static final int VERTICAL = 1;
    public static final int HORIZONTAL = 2;

    private Drawable mDivider;

    private int mLeftMargin, mRightMargin, mTopMargin, mBottomMargin;

    private int mWidth, mHeight;

    private int mOrientation;

    public DrawableItemDecoration(Drawable divider, int orientation) {
        setDivider(divider);
        setOrientation(orientation);
    }

    private void setDivider(Drawable divider) {
        this.mDivider = divider;
        if (mDivider == null) {
            mDivider = new ColorDrawable(0xffff0000);
        }
        mWidth = mDivider.getIntrinsicWidth();
        mHeight = mDivider.getIntrinsicHeight();
    }

    private void setOrientation(int orientation) {
        mOrientation = orientation;
    }

    public void setMargin(int left, int top, int right, int bottom) {
        this.mLeftMargin = left;
        this.mTopMargin = top;
        this.mRightMargin = right;
        this.mBottomMargin = bottom;
    }

    public void setHeight(int height) {
        this.mHeight = height;
    }

    public void setWidth(int width) {
        this.mWidth = width;
    }

    public int getHeight() {
        return mHeight;
    }

    public int getWidth() {
        return mWidth;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if ((mOrientation & HORIZONTAL) == HORIZONTAL) {
            drawHorizontal(c, parent);
        }
        if ((mOrientation & VERTICAL) == VERTICAL){
            drawVertical(c, parent);
        }
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop() + mTopMargin;
        final int bottom = parent.getHeight() - parent.getPaddingBottom() - mBottomMargin;

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getRight() + params.rightMargin + mLeftMargin;
            final int right = left + mWidth;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft() + mLeftMargin;
        final int right = parent.getWidth() - parent.getPaddingRight() - mRightMargin;

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin + mTopMargin;
            final int bottom = top + mHeight;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int vertical = 0, horizontal = 0;
        int itemPosition = parent.getChildAdapterPosition(view);
        int spanCount = getSpanCount(view, parent);
        int childCount = parent.getAdapter().getItemCount();

        if ((mOrientation & HORIZONTAL) == HORIZONTAL) {
            vertical = mLeftMargin + mWidth + mRightMargin;
        }
        if ((mOrientation & VERTICAL) == VERTICAL){
            horizontal = mTopMargin + mHeight + mBottomMargin;
        }

        if (isLastRaw(parent, view, itemPosition, spanCount, childCount)){// 如果是最后一行，则不需要绘制底部
            vertical = 0;
        }
        if (isLastColum(parent, view, itemPosition, spanCount, childCount)){// 如果是最后一列，则不需要绘制右边
            horizontal = 0;
        }
        outRect.set(0, 0, horizontal, vertical);
    }

    private int getSpanCount(View view, RecyclerView parent) {
        // 列数
        int spanCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager)layoutManager).getSpanCount();
        }
        else if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager.LayoutParams lp = (StaggeredGridLayoutManager.LayoutParams)
                    view.getLayoutParams();
            spanCount = lp.isFullSpan() ? 1 :((StaggeredGridLayoutManager) layoutManager)
                    .getSpanCount();
        }
        return spanCount;
    }

    private boolean isLastColum(RecyclerView parent, View view, int pos, int spanCount,
                                int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();

        if (layoutManager instanceof GridLayoutManager) {
            int spanIndex = ((GridLayoutManager.LayoutParams)view.getLayoutParams()).getSpanIndex();
            int spanSize = ((GridLayoutManager.LayoutParams)view.getLayoutParams()).getSpanSize();
            if ((spanIndex + spanSize) % spanCount == 0){// 如果是最后一列，则不需要绘制右边
                return true;
            }
        }
        else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            int spanIndex = ((StaggeredGridLayoutManager.LayoutParams)view.getLayoutParams()).getSpanIndex();

            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                if ((spanIndex + 1) % spanCount == 0){// 如果是最后一列，则不需要绘制右边
                    return true;
                }
            }
            else {
                if (pos + spanCount - spanIndex >= childCount) {// 如果是最后一列，则不需要绘制右边
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isLastRaw(RecyclerView parent, View view, int pos, int spanCount,
                              int childCount) {

        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            int spanIndex = ((GridLayoutManager.LayoutParams)view.getLayoutParams()).getSpanIndex();
            int spanSize = ((GridLayoutManager.LayoutParams)view.getLayoutParams()).getSpanSize();
            if (pos + spanCount - (spanIndex + spanSize - 1) >= childCount) {// 如果是最后一行，则不需要绘制底部
                return true;
            }
        }
        else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            int spanIndex = ((StaggeredGridLayoutManager.LayoutParams)view.getLayoutParams()).getSpanIndex();
            // StaggeredGridLayoutManager 且纵向滚动

            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                // 如果是最后一行，则不需要绘制底部
                if (pos + spanCount - spanIndex >= childCount) {
                    return true;
                }
            }
            else {// StaggeredGridLayoutManager 且横向滚动
                // 如果是最后一行，则不需要绘制底部
                if ((spanIndex + 1) % spanCount == 0) {
                    return true;
                }
            }
        }
        return false;
    }
}
