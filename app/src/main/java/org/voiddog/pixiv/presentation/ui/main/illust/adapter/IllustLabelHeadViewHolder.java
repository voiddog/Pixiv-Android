package org.voiddog.pixiv.presentation.ui.main.illust.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.voiddog.pixiv.R;
import org.voiddog.pixiv.data.model.illusts.LabelIllustModel;
import org.voiddog.pixiv.presentation.ui.common.view.PixivDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 插画头部view
 * Created by qgx44 on 2016/8/28.
 */
public class IllustLabelHeadViewHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.dv_img)
    PixivDraweeView mDvImg;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_name)
    TextView mTvName;

    LabelIllustModel mModel;

    public IllustLabelHeadViewHolder(View itemView) {
        super(itemView);
        StaggeredGridLayoutManager.LayoutParams layoutParams = new StaggeredGridLayoutManager.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setFullSpan(true);
        itemView.setLayoutParams(layoutParams);

        ButterKnife.bind(this, itemView);
        mTvTitle.setText("插画每日排行榜");
    }

    public void bindData(LabelIllustModel model){
        mModel = model;
        mDvImg.applyImageUrlModel(model.imageUrls);
        mTvName.setText(model.title);
    }
}
