package org.voiddog.imageselector.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import org.voiddog.imageselector.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by qigengxin on 16/9/7.
 */
public class SelectImgAdapter extends RecyclerView.Adapter<SelectImgViewHolder>{

    List<String> imgFilePathList = new ArrayList<>();
    Set<String> selectImages = new HashSet<>();
    OnSelectChangedListener selectChangedListener;
    RecyclerView recyclerView;

    /**
     * 绑定数据到adapter
     * @param imgFilePathList 文件名
     */
    public void setData(List<String> imgFilePathList){
        this.imgFilePathList = imgFilePathList;
        notifyDataSetChanged();
    }

    /**
     * 获取选择到的图片路径
     */
    public Set<String> getSelectImages(){
        return selectImages;
    }

    /**
     * 选择一张图片
     * @param path
     */
    public void select(String path){
        selectImages.add(path);
        if(recyclerView != null){
            for(int i = recyclerView.getChildCount() - 1; i >= 0; --i){
                View child = recyclerView.getChildAt(i);
                int itemPosition = recyclerView.getChildAdapterPosition(child);
                if(itemPosition < imgFilePathList.size()
                        && imgFilePathList.get(itemPosition).equals(path)){
                    RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(child);
                    if(viewHolder instanceof SelectImgViewHolder){
                        ((SelectImgViewHolder)viewHolder).select();
                    }
                    break;
                }
            }
        }
    }

    /**
     * 取消选择一张图片
     * @param path
     */
    public void unSelect(String path){
        selectImages.remove(path);
        if(recyclerView != null){
            for(int i = recyclerView.getChildCount() - 1; i >= 0; --i){
                View child = recyclerView.getChildAt(i);
                int itemPosition = recyclerView.getChildAdapterPosition(child);
                if(itemPosition < imgFilePathList.size()
                        && imgFilePathList.get(itemPosition).equals(path)){
                    RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(child);
                    if(viewHolder instanceof SelectImgViewHolder){
                        ((SelectImgViewHolder)viewHolder).unSelect();
                    }
                    break;
                }
            }
        }
    }

    /**
     * 清楚选择
     */
    public void clearSelect(){
        selectImages.clear();
        if(recyclerView != null){
            for(int i = recyclerView.getChildCount() - 1; i >= 0; --i){
                RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(
                        recyclerView.getChildAt(i)
                );
                if(viewHolder instanceof SelectImgViewHolder){
                    ((SelectImgViewHolder)viewHolder).unSelect();
                }
            }
        }
    }

    @Override
    public SelectImgViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.dl_is_item_select_img, null);
        SelectImgViewHolder vh = new SelectImgViewHolder(view);
        vh.setOnSelectChangeListener(new OnSelectChangedListener() {
            @Override
            public boolean selected(String path) {
                boolean ret = selectChangedListener == null || selectChangedListener.selected(path);
                if(ret){
                    selectImages.add(path);
                }
                return ret;
            }

            @Override
            public boolean unSelected(String path) {
                boolean ret = selectChangedListener == null || selectChangedListener.unSelected(path);
                if(ret){
                    selectImages.remove(path);
                }
                return ret;
            }
        });
        return vh;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = null;
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(SelectImgViewHolder holder, int position) {
        String path = imgFilePathList.get(position);
        holder.bindData(
                path, selectImages.contains(path)
        );
    }

    @Override
    public int getItemCount() {
        return imgFilePathList.size();
    }

    public void setOnSelectChangedListener(OnSelectChangedListener changedListener){
        selectChangedListener = changedListener;
    }

    public interface OnSelectChangedListener{
        boolean selected(String path);

        boolean unSelected(String path);
    }
}
