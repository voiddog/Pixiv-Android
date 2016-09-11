package org.voiddog.imageselector.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.voiddog.imageselector.R;
import org.voiddog.imageselector.data.AlbumItemData;
import org.voiddog.lib.ui.ResizeDraweeView;
import org.voiddog.lib.util.StringUtil;

/**
 * 相册ViewHolder
 * Created by qigengxin on 16/9/8.
 */
public class AlbumViewHolder extends RecyclerView.ViewHolder{
    // 相册预览的图片
    ResizeDraweeView dvFirst;
    // 相册名
    TextView tvAlbumName;

    AlbumItemData data;
    OnSelectAlbumCallback callback;

    public AlbumViewHolder(View itemView) {
        super(itemView);
        dvFirst = (ResizeDraweeView) itemView.findViewById(R.id.dv_fist);
        tvAlbumName = (TextView) itemView.findViewById(R.id.tv_album_name);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callback != null){
                    callback.onSelectAlbum(data);
                }
            }
        });
    }

    public void setOnSelectAlbumCallback(OnSelectAlbumCallback callback){
        this.callback = callback;
    }

    public void bindData(AlbumItemData data){
        this.data = data;
        dvFirst.setImageURI(StringUtil.getUriFromFilePath(data.fistImagePath));
        tvAlbumName.setText(String.format("%s(%d)", data.albumName, data.imageCount));
    }

    public interface OnSelectAlbumCallback{
        void onSelectAlbum(AlbumItemData data);
    }
}
