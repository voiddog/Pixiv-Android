package org.voiddog.lib.ui;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
        if ((mOrientation & HORIZONTAL) == HORIZONTAL) {
            vertical = mLeftMargin + mWidth + mRightMargin;
        }
        if ((mOrientation & VERTICAL) == VERTICAL){
            horizontal = mTopMargin + mHeight + mBottomMargin;
        }
        outRect.set(0, 0, vertical, horizontal);
    }
}
