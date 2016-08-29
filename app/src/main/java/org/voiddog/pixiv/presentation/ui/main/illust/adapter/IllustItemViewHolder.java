package org.voiddog.pixiv.presentation.ui.main.illust.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.voiddog.pixiv.R;
import org.voiddog.pixiv.data.model.IllustsModel;
import org.voiddog.pixiv.presentation.ui.common.view.PixivDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 小图 item view holder
 * Created by qigengxin on 16/8/26.
 */
public class IllustItemViewHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.dv_img)
    PixivDraweeView mDvImg;

    IllustsModel mModel;

    public IllustItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindData(IllustsModel model){
        mModel = model;
        mDvImg.applyImageUrlModel(model.imageUrls);
    }
}
