package org.voiddog.lib.ui;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;


/**
 * 默认图片大小根据view的大小改变
 * Created by voiddog on 2015/10/20.
 */
public class ResizeDraweeView extends SimpleDraweeView{

    private Uri mLazyUri;

    public ResizeDraweeView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
    }

    public ResizeDraweeView(Context context) {
        super(context);
    }

    public ResizeDraweeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ResizeDraweeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setImageURIWithSize(Uri uri, int width, int height){
        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(new ResizeOptions(width, height))
                .setAutoRotateEnabled(true)
                .build();
        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setOldController(getController())
                .setImageRequest(imageRequest)
                .build();

        setController(controller);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(getMeasuredWidth() != 0 && getMeasuredHeight() != 0
                && mLazyUri != null){
            setImageURIWithSize(mLazyUri, getMeasuredWidth(), getMeasuredHeight());
            mLazyUri = null;
        }
    }

    @Override
    public void setImageURI(Uri uri, Object callerContext) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        if(width == 0 || height == 0){
            mLazyUri = uri;
        }
        else{
            mLazyUri = null;
            setImageURIWithSize(uri, width, height);
        }
    }
}
