package org.voiddog.pixiv.presentation.ui.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.util.AttributeSet;

import com.facebook.drawee.generic.GenericDraweeHierarchy;

import org.voiddog.lib.ui.BlurLoadDraweeView;
import org.voiddog.pixiv.R;
import org.voiddog.pixiv.data.model.ImageUrlModel;

/**
 * 数据类型为
 * Created by qigengxin on 16/8/29.
 */
public class PixivDraweeView extends BlurLoadDraweeView{
    public static final int MEDIUM = 1;
    public static final int LARGE = 2;

    private int mSrcUriType = MEDIUM;

    public PixivDraweeView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
        init(context, null, 0);
    }

    public PixivDraweeView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public PixivDraweeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public PixivDraweeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    void init(Context context, AttributeSet attrs, int defStyle){
        final TypedArray a =  getContext().obtainStyledAttributes(
                attrs, R.styleable.PixivDraweeView, defStyle, 0
        );

        mSrcUriType = a.getInt(R.styleable.PixivDraweeView_pixiv_large_level, MEDIUM);

        a.recycle();
    }

    public void applyImageUrlModel(ImageUrlModel model){
        String[] urls = new String[]{
                model.squareMedium, model.medium, model.large, model.original
        };
        String smallUrl = null;
        for(String url : urls){
            if(url != null){
                smallUrl = url;
                break;
            }
        }

        String largeUrl = null;
        for(int i = mSrcUriType == MEDIUM ? 1 : 2; i < urls.length; ++i){
            if(urls[i] != null){
                largeUrl = urls[i];
                break;
            }
        }
        if(largeUrl == null){
            largeUrl = smallUrl;
        }

        loadUri(Uri.parse(smallUrl), Uri.parse(largeUrl));
    }
}
