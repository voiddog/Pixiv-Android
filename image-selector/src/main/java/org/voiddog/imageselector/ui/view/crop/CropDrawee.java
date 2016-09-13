package org.voiddog.imageselector.ui.view.crop;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import org.voiddog.lib.gesture.MoveGestureDetector;
import org.voiddog.lib.util.BlurUtil;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 文件描述
 *
 * @author qigengxin
 * @since 2016-09-10 22:09
 */

public class CropDrawee extends View{

    Bitmap smallBitmap;
    Bitmap originBitmap;
    int originWidth, originHeight;
    MoveGestureDetector moveGestureDetector;
    ScaleGestureDetector scaleGestureDetector;
    Matrix moveMatrix, baseMatrix, scaleMatrix;
    Matrix tmpMatrix;
    Matrix originBaseMatrix;
    BitmapRegionDecoder decoder;

    public CropDrawee(Context context) {
        super(context);
        init(context, null, 0);
    }

    public CropDrawee(Context context, AttributeSet attr) {
        super(context, attr);
        init(context, attr, 0);
    }

    public CropDrawee(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
        init(context, attr, defStyle);
    }

    void init(Context context, AttributeSet attr, int defStyle){
        moveGestureDetector = new MoveGestureDetector(context, new MoveListener());
        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());

        moveMatrix = new Matrix();
        baseMatrix = new Matrix();
        scaleMatrix = new Matrix();
        tmpMatrix = new Matrix();

        originBaseMatrix = new Matrix();
    }

    class MoveListener implements MoveGestureDetector.OnMoveGestureListener{


        @Override
        public boolean onMove(MoveGestureDetector detector) {
            moveMatrix.postTranslate(detector.getFocusDelta().x, detector.getFocusDelta().y);
            invalidate();
            return true;
        }

        @Override
        public boolean onMoveBegin(MoveGestureDetector detector) {
            return true;
        }

        @Override
        public void onMoveEnd(MoveGestureDetector detector) {
            updateOrigin();
            invalidate();
        }
    }

    class ScaleListener implements ScaleGestureDetector.OnScaleGestureListener{

        float baseValue[] = new float[9];
        float moveValue[] = new float[9];

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            // 计算中心点
            baseMatrix.getValues(baseValue);
            moveMatrix.getValues(moveValue);
            float bitmapLeft = baseValue[Matrix.MTRANS_X] + moveValue[Matrix.MTRANS_X];
            float bitmapTop = baseValue[Matrix.MTRANS_Y] + moveValue[Matrix.MTRANS_Y];

            scaleMatrix.postScale(
                    detector.getScaleFactor(), detector.getScaleFactor(),
                    detector.getFocusX() - bitmapLeft, detector.getFocusY() - bitmapTop
            );
            invalidate();
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            updateOrigin();
            invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        moveGestureDetector.onTouchEvent(event);
        scaleGestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }

    public void setImageURI(Uri uri) {
        Observable.just(uri)
                .subscribeOn(Schedulers.io())
                .map(new Func1<Uri, Uri>() {
                    @Override
                    public Uri call(Uri uri) {
                        try {
                            setupDecoder(
                                    getContext().getContentResolver().openInputStream(uri)
                            );
                            setInputStream(
                                    getContext().getContentResolver().openInputStream(uri),
                                    calculateSampleSize(getContext().getContentResolver().openInputStream(uri))
                            );
                            return uri;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Uri>() {
                    @Override
                    public void onCompleted() {
                        invalidate();
                    }

                    @Override
                    public void onError(Throwable e) {}

                    @Override
                    public void onNext(Uri uri) {}
                });
    }

    void setupDecoder(InputStream inputStream) throws IOException {
        try {
            decoder = BitmapRegionDecoder.newInstance(inputStream, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            inputStream.close();
        }
    }

    int calculateSampleSize(InputStream inputStream) throws IOException {
        BitmapFactory.Options tmpOptions = new BitmapFactory.Options();
        tmpOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(new BufferedInputStream(inputStream), null, tmpOptions);
        originWidth = tmpOptions.outWidth;
        originHeight = tmpOptions.outHeight;
        inputStream.close();
        return calculateInSampleSize(tmpOptions.outWidth, tmpOptions.outHeight
                , getMeasuredWidth(), getMeasuredHeight());
    }

    void setInputStream(InputStream inputStream, int inSampleSize) throws IOException {
        BitmapFactory.Options tmpOptions = new BitmapFactory.Options();
        tmpOptions.inSampleSize = inSampleSize;
        smallBitmap = BitmapFactory.decodeStream(inputStream, null, tmpOptions);
        if(smallBitmap != null) {
            smallBitmap = BlurUtil.blurWithRenderScript(
                    getContext(), smallBitmap, 12, true
            );
            float maxScale = Math.max(
                    1.0f * getMeasuredWidth() / smallBitmap.getWidth(), 1.0f * getMeasuredHeight() / smallBitmap.getHeight()
            );
            baseMatrix.setTranslate(getMeasuredWidth()/2 - smallBitmap.getWidth()/2*maxScale
                    , getMeasuredHeight()/2 - smallBitmap.getHeight()/2*maxScale);
            baseMatrix.postScale(maxScale, maxScale);
            moveMatrix.setTranslate(0, 0);
            scaleMatrix.setScale(1, 1);
            originBitmap = null;
            updateOrigin();
        }
        inputStream.close();
    }

    int calculateInSampleSize(int width, int height, int reqWidth, int reqHeight) {
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    BitmapFactory.Options originOptions = new BitmapFactory.Options();
    Rect originRect = new Rect();
    float tmpValue[] = new float[9];

    /**
     * 更新显示区域中的原图
     */
    void updateOrigin(){
        tmpMatrix.set(baseMatrix);
        tmpMatrix.postConcat(scaleMatrix);
        tmpMatrix.postConcat(moveMatrix);

        // 计算缩放后的大小
        tmpMatrix.getValues(tmpValue);
        int left = (int) tmpValue[Matrix.MTRANS_X];
        int top = (int) tmpValue[Matrix.MTRANS_Y];
        int right = (int) (smallBitmap.getWidth()*tmpValue[Matrix.MSCALE_X] + tmpValue[Matrix.MTRANS_X]);
        int bottom = (int) (smallBitmap.getHeight()*tmpValue[Matrix.MSCALE_Y] + tmpValue[Matrix.MTRANS_Y]);

        int inSampleSize = calculateInSampleSize(originWidth, originHeight,
                right - left, bottom - top);
        // 计算截图区域
        originRect.left = Math.max(-left, 0);
        originRect.top = Math.max(-top, 0);
        originRect.right = Math.min(right - left, getMeasuredWidth() - left);
        originRect.bottom = Math.min(bottom - top, getMeasuredHeight() - top);
        if(originRect.right - originRect.left > 0
                && originRect.bottom - originRect.top > 0) {
            int width = Math.round(originWidth * 1.0f / inSampleSize);
            originRect.left = Math.round(originRect.left * 1.0f * originWidth / (right - left));
            originRect.top = Math.round(originRect.top * 1.0f * originHeight / (bottom - top));
            originRect.right = Math.round(originRect.right * 1.0f * originWidth / (right - left));
            originRect.bottom = Math.round(originRect.bottom * 1.0f * originHeight / (bottom - top));

            originOptions.inSampleSize = inSampleSize;
            originBitmap = decoder.decodeRegion(originRect, originOptions);
            float scale = smallBitmap.getWidth() * 1.0f / width;
            originBaseMatrix.setScale(scale, scale);
            originBaseMatrix.postTranslate(
                    originRect.left * scale, originRect.top * scale
            );
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(smallBitmap != null){
            // 绘制底部
            tmpMatrix.set(baseMatrix);
            tmpMatrix.postConcat(scaleMatrix);
            tmpMatrix.postConcat(moveMatrix);
            canvas.drawBitmap(smallBitmap, tmpMatrix, null);

        }
        if(originBitmap != null){
            tmpMatrix.set(originBaseMatrix);
            tmpMatrix.postConcat(baseMatrix);
            tmpMatrix.postConcat(scaleMatrix);
            tmpMatrix.postConcat(moveMatrix);
            canvas.drawBitmap(originBitmap, tmpMatrix, null);
        }
    }
}
