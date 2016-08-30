package org.voiddog.pixiv.presentation.ui.main.illust;

import android.content.Context;
import android.text.TextUtils;

import org.voiddog.lib.mvp.lce.MvpLceRxPresenter;
import org.voiddog.pixiv.data.api.IllustsApi;
import org.voiddog.pixiv.data.model.RankingModel;
import org.voiddog.pixiv.presentation.ui.common.activity.base.ForActivity;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by qigengxin on 16/8/26.
 */
public class IllustPresenter extends MvpLceRxPresenter<RankingModel, IllustView>{

    @Inject
    @ForActivity
    Context mContext;

    @Inject
    IllustsApi mApi;

    private String mNextUrl;

    public void loadData(boolean refresh){
        Observable<RankingModel> request = null;
        if(refresh){
            mNextUrl = null;
            request = mApi.rankListWithoutLogin();
            getView().showLoading(mNextUrl == null);
        }
        else if(!TextUtils.isEmpty(mNextUrl)){
            request = mApi.next(mNextUrl);
            getView().showLoading(false);
        }
        if(request != null) {
            subscribe(request, refresh);
        }
    }

    @Override
    protected void onNext(RankingModel data, boolean pullToRefresh) {
        if(!TextUtils.isEmpty(data.nextUrl)){
            mNextUrl = data.nextUrl;
        }
        else{
            mNextUrl = null;
        }
        super.onNext(data, pullToRefresh);
    }
}
