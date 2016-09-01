package org.voiddog.pixiv.presentation.ui.main.illust.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.voiddog.pixiv.R;
import org.voiddog.pixiv.data.model.illusts.IllustsModel;
import org.voiddog.pixiv.presentation.ui.common.event.IllustsBookmarkEvent;
import org.voiddog.pixiv.presentation.ui.common.view.LikeButton;
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
    @BindView(R.id.btn_like)
    LikeButton mLikeBtn;

    IllustsModel mModel;

    public IllustItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        mLikeBtn.setOnLikeListener(new LikeButton.OnLikeListener() {
            @Override
            public void onLikeChanged(boolean liked) {
                mModel.isBookmarked = liked;
                IllustsBookmarkEvent event = new IllustsBookmarkEvent();
                event.id = String.valueOf(mModel.id);
                event.isAdd = liked;
                EventBus.getDefault().post(event);
            }
        });
    }

    public void bindData(IllustsModel model){
        mModel = model;
        mDvImg.setAspectRatio(1.0f * model.width / model.height);
        mDvImg.applyImageUrlModel(model.imageUrls);
        mLikeBtn.setLikeState(model.isBookmarked);
    }
}
