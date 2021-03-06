package org.voiddog.pixiv.presentation.ui.common.fragment.lce;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.voiddog.lib.mvp.lce.MvpLceFragment;
import org.voiddog.lib.mvp.lce.MvpLceRxPresenter;
import org.voiddog.lib.mvp.lce.MvpLceView;
import org.voiddog.pixiv.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 下拉刷新 底部加载更多 Fragment
 * 需要子类提供注入 {@link android.support.v7.widget.RecyclerView.Adapter},
 * {@link android.support.v7.widget.RecyclerView.LayoutManager}, {@link ILceDataHelper}
 * Created by qigengxin on 16/8/26.
 */
public abstract class LceRecycleFragment<M, V extends MvpLceView<M>, P extends MvpLceRxPresenter<M, V>>
        extends MvpLceFragment<M, V, P>{

    @BindView(R.id.pb_loading)
    protected ProgressBar mPbLoading;
    @BindView(R.id.tv_error)
    protected TextView mTvError;
    @BindView(R.id.vg_error)
    protected ViewGroup mVgError;
    @BindView(R.id.srl_lce)
    protected SwipeRefreshLayout mSrlLce;
    @BindView(R.id.rv_content)
    protected RecyclerView mRvContent;
    @Inject
    protected RecyclerView.Adapter mAdapter;
    @Inject
    protected RecyclerView.LayoutManager mLayoutManager;
    @Inject
    protected P mPresenter;
    @Inject
    protected ILceDataHelper<M> mDataHelper;

    private boolean mIsLoadingData = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lce_recycle, null);
        ButterKnife.bind(this, view);
        setupViews(savedInstanceState);
        return view;
    }

    /**
     * 初始化创建的视图
     */
    void setupViews(Bundle saveInstanceState){
        Resources resources = getActivity().getResources();
        mSrlLce.setColorSchemeColors(
                resources.getColor(R.color.google_red),
                resources.getColor(R.color.google_blue),
                resources.getColor(R.color.google_green),
                resources.getColor(R.color.google_yellow)
        );
        mSrlLce.setOnRefreshListener(new RefreshListenerImpl());

        mRvContent.setLayoutManager(mLayoutManager);
        mRvContent.setAdapter(mAdapter);
        mRvContent.addOnScrollListener(new BottomListener());
    }

    protected boolean isLoadingData(){
        return mIsLoadingData;
    }

    @Override
    protected View createLoadingView() {
        return mPbLoading;
    }

    @Override
    protected View createContentView() {
        return mRvContent;
    }

    @Override
    protected View createErrorView() {
        return mVgError;
    }

    @Override
    public P createPresenter() {
        return mPresenter;
    }

    @Override
    protected void showPageError(String msg) {
        mIsLoadingData = false;
        mTvError.setText(msg);
    }

    @Override
    public void setData(M data) {
        mIsLoadingData = false;
        mDataHelper.setData(data);
    }

    @Override
    public void addData(M data) {
        mIsLoadingData = false;
        mDataHelper.addData(data);
    }

    class RefreshListenerImpl implements SwipeRefreshLayout.OnRefreshListener{

        @Override
        public void onRefresh() {
            getPresenter().unsubscribe();
            mSrlLce.setRefreshing(false);
            mIsLoadingData = true;
            loadData(true);
        }
    }

    class BottomListener extends RecyclerView.OnScrollListener{
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if(mDataHelper.isNeedLoadMore() && !isLoadingData()){
                mIsLoadingData = true;
                loadData(false);
            }
        }
    }
}
