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
    ProgressBar mPbLoading;
    @BindView(R.id.tv_error)
    TextView mTvError;
    @BindView(R.id.srl_lce)
    SwipeRefreshLayout mSrlLce;
    @BindView(R.id.rv_content)
    RecyclerView mRvContent;
    @Inject
    RecyclerView.Adapter mAdapter;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    P mPresenter;
    @Inject
    ILceDataHelper<M> mDataHelper;

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
        return mTvError;
    }

    @Override
    public P createPresenter() {
        return mPresenter;
    }

    @Override
    protected void showPageError(String msg) {
        mTvError.setText(msg);
    }

    @Override
    public void setData(M data) {
        mDataHelper.setData(data);
    }

    @Override
    public void addData(M data) {
        mDataHelper.addData(data);
    }

    class RefreshListenerImpl implements SwipeRefreshLayout.OnRefreshListener{

        @Override
        public void onRefresh() {
            loadData(true);
        }
    }
}
