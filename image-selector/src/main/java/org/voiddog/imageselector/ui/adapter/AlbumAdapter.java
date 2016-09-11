package org.voiddog.imageselector.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import org.voiddog.imageselector.R;
import org.voiddog.imageselector.data.AlbumItemData;

import java.util.List;

/**
 * Created by qigengxin on 16/9/8.
 */
public class AlbumAdapter extends RecyclerView.Adapter<AlbumViewHolder>
        implements AlbumViewHolder.OnSelectAlbumCallback{

    List<AlbumItemData> albumItemDataList;
    AlbumViewHolder.OnSelectAlbumCallback callback;

    public void setData(List<AlbumItemData> albumItemDataList){
        this.albumItemDataList = albumItemDataList;
        notifyDataSetChanged();
    }

    @Override
    public AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.dl_is_item_album, null);
        AlbumViewHolder vh = new AlbumViewHolder(view);
        vh.setOnSelectAlbumCallback(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(AlbumViewHolder holder, int position) {
        holder.bindData(albumItemDataList.get(position));
    }

    @Override
    public int getItemCount() {
        return albumItemDataList == null ? 0 : albumItemDataList.size();
    }

    @Override
    public void onSelectAlbum(AlbumItemData data) {
        if(callback != null){
            callback.onSelectAlbum(data);
        }
    }

    public void setOnSelectCallback(AlbumViewHolder.OnSelectAlbumCallback callback){
        this.callback = callback;
    }
}
