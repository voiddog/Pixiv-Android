package org.voiddog.lib.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import org.voiddog.lib.util.BlurUtil;

/**
 * 模糊当前块的视图
 * Created by qgx44 on 2016/9/2.
 */
public class BlurView extends View{
    private static final int MAX_BLUR_SIZE = 100;

    private boolean mIsPreDraw = false;
    private Bitmap mBlurBitmap = null;
    private Canvas mBlurCanvas = null;
    private View mContentView = null;

    public BlurView(Context context) {
        super(context);
    }

    public BlurView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BlurView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setContentView(View contentView){
        mContentView = contentView;
    }

    public void updateBitmap(float radius){
        View contentView = mContentView == null ? getContentView() : mContentView;
        if(!mIsPreDraw && mBlurBitmap != null
                && contentView != null){
            mIsPreDraw = true;

            mBlurCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            int l = getRawX(), t = getRawY();

            int saveCount = mBlurCanvas.save();
            mBlurCanvas.translate(-l, -t);
            mBlurCanvas.clipRect(l, t, l + getWidth(), t + getHeight());
            contentView.draw(mBlurCanvas);
            mBlurCanvas.restoreToCount(saveCount);

            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                BlurUtil.blurWithRenderScript(getContext(), mBlurBitmap, radius, true);
            }
            else{
                BlurUtil.fastBlur(mBlurBitmap, (int) radius, true);
            }

            mIsPreDraw = false;
        }
    }

    public void updateBitmap(){
        updateBitmap(12);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(getMeasuredWidth() == 0 || getMeasuredHeight() == 0){
            return;
        }

        float scale = getScale(getMeasuredWidth(), getMeasuredHeight());
        int width = (int) (scale * getMeasuredWidth()), height = (int) (scale * getMeasuredHeight());
        if(mBlurBitmap == null || mBlurBitmap.getWidth() != width
                || mBlurBitmap.getHeight() != height){
            mBlurBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            mBlurCanvas = new Canvas(mBlurBitmap);
            mBlurCanvas.scale(scale, scale);
            updateBitmap();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(!mIsPreDraw && mBlurBitmap != null){
            canvas.save();
            float scale = 1.0f / getScale(getWidth(), getHeight());
            canvas.scale(scale, scale);
            canvas.drawBitmap(mBlurBitmap, 0, 0, null);
            canvas.restore();
        }
    }

    private int getRawX(){
        int res = 0;
        View view = this;
        while(view != null
                && view.getId() != android.R.id.content){
            res += view.getX();
            if(view.getParent() instanceof View) {
                view = (View) view.getParent();
            }
            else{
                break;
            }
        }
        return res;
    }

    private int getRawY(){
        int res = 0;
        View view = this;
        while(view != null
                && view.getId() != android.R.id.content){
            res += view.getY();
            if(view.getParent() instanceof View) {
                view = (View) view.getParent();
            }
            else{
                break;
            }
        }
        return res;
    }

    private View getContentView(){
        View res = this;
        while(res != null && res.getId() != android.R.id.content){
            if(res.getParent() instanceof View) {
                res = (View) res.getParent();
            }
            else{
                break;
            }
        }
        return res;
    }

    private float getScale(int width, int height){
        return Math.min(
                1, Math.min(1.0f * MAX_BLUR_SIZE / width, 1.0f * MAX_BLUR_SIZE / height)
        );
    }
}
