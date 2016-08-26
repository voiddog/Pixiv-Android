package org.voiddog.lib.adapter;

import android.support.v7.widget.RecyclerView;

import org.voiddog.lib.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qigengxin on 16/8/26.
 */
public abstract class RecycleCommonAdapter<VH extends RecyclerView.ViewHolder, M>
        extends RecyclerView.Adapter<VH>{
    public List<M> mDataList = new ArrayList<>();

    public void setData(M data){
        mDataList.add(data);
    }

    public void setDataList(List<M> dataList){
        this.mDataList = dataList;
    }

    public void addDataList(int position, List<M> dataList){
        this.mDataList.addAll(position, dataList);
    }

    public void addDataList(List<M> dataList){
        this.mDataList.addAll(dataList);
    }

    public void addData(M data){
        this.mDataList.add(data);
    }

    public void addData(int positoin, M data){
        this.mDataList.add(positoin, data);
    }

    public void clearData(){
        this.mDataList.clear();
    }

    public void removeData(int position){
        if(position >= mDataList.size()){
            LogUtil.E("RecycleCommonAdapter", "越界访问");
            return;
        }

        this.mDataList.remove(position);
    }

    public void removeData(M data){
        mDataList.remove(data);
    }

    public List<M> getDataList(){
        return mDataList;
    }
}
