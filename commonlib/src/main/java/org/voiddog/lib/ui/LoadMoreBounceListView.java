package org.voiddog.lib.ui;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import org.voiddog.lib.util.SizeUtil;

/**
 * 有底部加载更多字样的list view，滑动到底部的时候，调用加载更多的listener
 * Created by Dog on 2015/5/20.
 */
public class LoadMoreBounceListView extends BounceListView{

    TextView footView;
    String mLoadingText = "正在加载";
    String mLoadComplete = "没有更多了";
    String mLoadError = "加载失败";
    int mLastVisibleItem, mTotalVisibleItem;
    boolean mIsLoadComplete = false;
    OnLoadMoreListener mLoadMoreListener = null;

    public LoadMoreBounceListView(Context context) {
        super(context);
        initRoot();
    }

    public LoadMoreBounceListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initRoot();
    }

    public LoadMoreBounceListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initRoot();
    }

    public interface OnLoadMoreListener{
        void onLoadMore();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener){
        this.mLoadMoreListener = onLoadMoreListener;
    }

    /**
     * 设置是否加载完全
     * @param isLoadComplete true or false
     */
    public void setLoadComplete(boolean isLoadComplete){
        this.mIsLoadComplete = isLoadComplete;
        if(isLoadComplete){
            footView.setText(mLoadComplete);
        }
        else{
            footView.setText(mLoadingText);
        }
    }

    public void setLoadError(){
        footView.setText(mLoadError);
    }

    /**
     * 初始化root view，初始化底部的view
     */
    void initRoot(){
        int padding = SizeUtil.dp2px(getContext(), 20);
        footView = new TextView(getContext());
        footView.setText(mLoadingText);
        footView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        footView.setPadding(padding, padding, padding, padding);
        footView.setGravity(Gravity.CENTER);
        footView.setTextColor(Color.GRAY);
        footView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        this.setOnScrollListener(new LoadMoreListScrollListener());
        addFooterView(footView);
    }

    class LoadMoreListScrollListener implements OnScrollListener{

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if(scrollState == SCROLL_STATE_IDLE && mLastVisibleItem == mTotalVisibleItem){
                if(mLoadMoreListener != null && !mIsLoadComplete){
                    mLoadMoreListener.onLoadMore();
                }
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            LoadMoreBounceListView.this.mLastVisibleItem = firstVisibleItem + visibleItemCount;
            LoadMoreBounceListView.this.mTotalVisibleItem = totalItemCount;
        }
    }
}
