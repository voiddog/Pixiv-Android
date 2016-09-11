package org.voiddog.imageselector.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import org.voiddog.imageselector.R;
import org.voiddog.lib.ui.ResizeDraweeView;
import org.voiddog.lib.util.StringUtil;

/**
 * 选择图片recycle view 的view holder
 * Created by qigengxin on 16/9/7.
 */
public class SelectImgViewHolder extends RecyclerView.ViewHolder{

    ResizeDraweeView dvImg;
    ImageView ivSelected;
    SelectImgAdapter.OnSelectChangedListener changedListener;
    String path;

    public SelectImgViewHolder(View itemView) {
        super(itemView);
        dvImg = (ResizeDraweeView) itemView.findViewById(R.id.dl_is_dv_img);
        ivSelected = (ImageView) itemView.findViewById(R.id.dl_is_iv_selected);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSelected()){
                    if(changedListener == null
                            || changedListener.unSelected(path)){
                        unSelect();
                    }
                }
                else{
                    if(changedListener == null
                            || changedListener.selected(path)){
                        select();
                    }
                }
            }
        });
    }

    public void bindData(String imgPath, boolean selected){
        path = imgPath;
        if(selected){
            select();
        }
        else{
            unSelect();
        }

        dvImg.setImageURI(StringUtil.getUriFromFilePath(imgPath));
    }

    void select(){
        ivSelected.setVisibility(View.VISIBLE);
    }

    void unSelect(){
        ivSelected.setVisibility(View.INVISIBLE);
    }

    boolean isSelected(){
        return ivSelected.getVisibility() == View.VISIBLE;
    }

    public void setOnSelectChangeListener(SelectImgAdapter.OnSelectChangedListener changeListener){
        this.changedListener = changeListener;
    }
}
