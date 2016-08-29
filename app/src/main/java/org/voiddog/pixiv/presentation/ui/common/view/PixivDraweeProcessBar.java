package org.voiddog.pixiv.presentation.ui.common.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import org.voiddog.lib.util.SizeUtil;

/**
 * Created by qigengxin on 16/8/29.
 */
public class PixivDraweeProcessBar extends Drawable{

    private int mCurrentLevel = 0;

    private Paint mPaint;

    private static float MAX_LEVEL = 10000;

    private RectF mDrawRect = new RectF();

    public PixivDraweeProcessBar(Context context){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(SizeUtil.dp2px(context, 1));
        mPaint.setColor(0xffffffff);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void draw(Canvas canvas) {
        Rect rect = getBounds();
        int minSize = Math.min(rect.right - rect.left, rect.bottom - rect.top);
        int R = minSize >> 2;
        int withSpace = (rect.right - rect.left - (R << 1)) >> 1;
        int heightSpace = (rect.bottom - rect.top - (R << 1)) >> 1;
        mDrawRect.left = rect.left + withSpace;
        mDrawRect.top = rect.top + heightSpace;
        mDrawRect.right = rect.right - withSpace;
        mDrawRect.bottom = rect.bottom - heightSpace;
        canvas.drawArc(mDrawRect, 0, 1.0f * mCurrentLevel / MAX_LEVEL * 360, false, mPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    protected boolean onLevelChange(int level) {
        if(mCurrentLevel != level){
            mCurrentLevel = level;
            return true;
        }
        return false;
    }
}
