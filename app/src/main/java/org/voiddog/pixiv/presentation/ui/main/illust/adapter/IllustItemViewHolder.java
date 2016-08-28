package org.voiddog.pixiv.presentation.ui.main.illust.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.voiddog.lib.ui.ResizeDraweeView;
import org.voiddog.pixiv.R;
import org.voiddog.pixiv.data.model.IllustsModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 小图 item view holder
 * Created by qigengxin on 16/8/26.
 */
public class IllustItemViewHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.dv_img)
    ResizeDraweeView mDvImg;

    IllustsModel mModel;

    public IllustItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindData(IllustsModel model){
        mModel = model;
        if(model.imageUrls.medium != null){
            mDvImg.setImageURI(Uri.parse(model.imageUrls.medium));
        }
        else if(model.imageUrls.large != null){
            mDvImg.setImageURI(Uri.parse(model.imageUrls.large));
        }
    }
}
