package org.voiddog.lib.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import org.voiddog.lib.util.BlurUtil;

/**
 * 模糊加载图片
 * Created by qigengxin on 16/8/29.
 */
public class BlurLoadDraweeView extends ResizeDraweeView{

    private static final int BLUR_SIZE = 100;

    private LoadUri mLoadUri;

    public BlurLoadDraweeView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
    }

    public BlurLoadDraweeView(Context context) {
        super(context);
    }

    public BlurLoadDraweeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BlurLoadDraweeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(getMeasuredWidth() > 0 && getMeasuredHeight() > 0
                && mLoadUri != null){
            applyRequest(mLoadUri.smallUri, mLoadUri.largeUri);
        }
    }

    public void loadUri(Uri smallUri, Uri largeUri){
        if(getMeasuredWidth() <= 0 || getMeasuredHeight() <= 0){
            mLoadUri = new LoadUri();
            mLoadUri.smallUri = smallUri;
            mLoadUri.largeUri = largeUri;
        }
        else{
            mLoadUri = null;
            applyRequest(smallUri, largeUri);
        }
    }

    /**
     * please use {@link #loadUri(Uri, Uri)}
     * @param uri
     */
    @Deprecated
    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
    }

    void applyRequest(Uri smallUri, Uri largeUri){
        if(smallUri == null
                || smallUri.toString().equals(largeUri.toString())){
            super.setImageURI(largeUri);
            return;
        }

        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setOldController(getController())
                .setLowResImageRequest(getBlurImageRequest(smallUri))
                .setImageRequest(getResizeImageRequest(largeUri, getMeasuredWidth(), getMeasuredHeight()))
                .build();

        setController(controller);
    }

    ImageRequest getBlurImageRequest(Uri uri){
        return ImageRequestBuilder.newBuilderWithSource(uri)
                .setPostprocessor(new BlurPostProcessor())
                .setResizeOptions(new ResizeOptions(BLUR_SIZE, BLUR_SIZE))
                .build();
    }

    ImageRequest getResizeImageRequest(Uri uri, int width, int height){
        return ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(new ResizeOptions(width, height))
                .build();
    }

    class BlurPostProcessor extends BasePostprocessor{
        @Override
        public String getName() {
            return "BlurPostProcessor";
        }

        @Override
        public void process(Bitmap bitmap) {
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN){
                BlurUtil.blurWithRenderScript(getContext(), bitmap, 8, true);
            }
            else{
                BlurUtil.fastBlur(bitmap, 8, true);
            }
        }
    }

    class LoadUri{
        public Uri smallUri;
        public Uri largeUri;
    }
}
