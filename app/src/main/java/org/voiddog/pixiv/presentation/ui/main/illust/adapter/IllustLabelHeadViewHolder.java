package org.voiddog.pixiv.presentation.ui.main.illust.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.voiddog.lib.ui.ResizeDraweeView;
import org.voiddog.pixiv.R;
import org.voiddog.pixiv.data.model.LabelIllustModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 插画头部view
 * Created by qgx44 on 2016/8/28.
 */
public class IllustLabelHeadViewHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.dv_img)
    ResizeDraweeView mDvImg;

    LabelIllustModel mModel;

    public IllustLabelHeadViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindData(LabelIllustModel model){
        mModel = model;
        if(model.imageUrls.medium != null){
            mDvImg.setImageURI(Uri.parse(model.imageUrls.medium));
        }
        else if(model.imageUrls.large != null){
            mDvImg.setImageURI(Uri.parse(model.imageUrls.large));
        }
    }
}
