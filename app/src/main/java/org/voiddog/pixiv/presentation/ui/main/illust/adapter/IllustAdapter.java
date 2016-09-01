package org.voiddog.pixiv.presentation.ui.main.illust.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import org.voiddog.pixiv.R;
import org.voiddog.pixiv.data.model.illusts.IllustsRankingModel;
import org.voiddog.pixiv.data.model.illusts.IllustsModel;
import org.voiddog.pixiv.data.model.illusts.LabelIllustModel;
import org.voiddog.pixiv.presentation.ui.common.fragment.lce.ILceDataHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qigengxin on 16/8/26.
 */
public class IllustAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements ILceDataHelper<IllustsRankingModel>{
    // 头部label插图
    private static final int TYPE_LABEL = 1;
    // item元素
    private static final int TYPE_ILLUST = 2;

    List<Object> mDataList = new ArrayList<>();

    StaggeredGridLayoutManager mLayoutManager;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(parent instanceof RecyclerView){
            RecyclerView recyclerView = (RecyclerView) parent;
            if(recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager){
                mLayoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
            }
        }
        if(viewType == TYPE_ILLUST){
            return new IllustItemViewHolder(View.inflate(
                    parent.getContext(), R.layout.item_like_img, null
            ));
        } else if (viewType == TYPE_LABEL){
            return new IllustLabelHeadViewHolder(View.inflate(
                    parent.getContext(), R.layout.item_label_head, null
            ));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof IllustItemViewHolder){
            IllustItemViewHolder illustItemViewHolder = (IllustItemViewHolder) holder;
            illustItemViewHolder.bindData((IllustsModel) mDataList.get(position));
        } else if (holder instanceof IllustLabelHeadViewHolder){
            IllustLabelHeadViewHolder illustLabelHeadViewHolder = (IllustLabelHeadViewHolder) holder;
            illustLabelHeadViewHolder.bindData((LabelIllustModel) mDataList.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(mDataList.get(position) instanceof LabelIllustModel){
            return TYPE_LABEL;
        }
        else if(mDataList.get(position) instanceof IllustsModel){
            return TYPE_ILLUST;
        }
        throw new IllegalArgumentException("不支持的数据类型");
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public void addData(IllustsRankingModel data) {
        if(data.rankingLabelIllust != null){
            mDataList.add(data.rankingLabelIllust);
        }
        for(IllustsModel model : data.illusts){
            mDataList.add(model);
        }
        notifyDataSetChanged();
    }

    @Override
    public void setData(IllustsRankingModel data) {
        mDataList.clear();
        if(data.rankingLabelIllust != null){
            mDataList.add(data.rankingLabelIllust);
        }
        for(IllustsModel model : data.illusts){
            mDataList.add(model);
        }
        notifyDataSetChanged();
    }

    int[] lastVisiblePosition;

    @Override
    public boolean isNeedLoadMore() {
        if(mLayoutManager != null){
            if(lastVisiblePosition == null
                    || lastVisiblePosition.length != mLayoutManager.getSpanCount()){
                lastVisiblePosition = new int[mLayoutManager.getSpanCount()];
            }
            mLayoutManager.findLastVisibleItemPositions(lastVisiblePosition);
            for(int position : lastVisiblePosition){
                if(position == getItemCount() - 1){
                    return true;
                }
            }
        }
        return false;
    }
}
