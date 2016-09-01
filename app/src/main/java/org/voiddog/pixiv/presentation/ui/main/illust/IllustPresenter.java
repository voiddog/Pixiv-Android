package org.voiddog.pixiv.presentation.ui.main.illust;

import android.content.Context;
import android.text.TextUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.voiddog.lib.mvp.lce.MvpLceRxPresenter;
import org.voiddog.pixiv.data.api.IllustsApi;
import org.voiddog.pixiv.data.model.illusts.IllustsRankingModel;
import org.voiddog.pixiv.presentation.ui.common.activity.base.ForActivity;
import org.voiddog.pixiv.presentation.ui.common.event.IllustsBookmarkEvent;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by qigengxin on 16/8/26.
 */
public class IllustPresenter extends MvpLceRxPresenter<IllustsRankingModel, IllustView>{

    @Inject
    @ForActivity
    Context mContext;

    @Inject
    IllustsApi mApi;

    private String mNextUrl;

    public IllustPresenter(){
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onEvent(IllustsBookmarkEvent event){
        if(event.isAdd) {
            mApi.addBookMark(event.id, "public").subscribe();
        }
        else{
            mApi.deleteBookMark(event.id).subscribe();
        }
    }

    @Override
    public void onDestory() {
        super.onDestory();
        EventBus.getDefault().unregister(this);
    }

    public void loadData(boolean refresh){
        Observable<IllustsRankingModel> request = null;
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
    protected void onNext(IllustsRankingModel data, boolean pullToRefresh) {
        if(!TextUtils.isEmpty(data.nextUrl)){
            mNextUrl = data.nextUrl;
        }
        else{
            mNextUrl = null;
        }
        super.onNext(data, pullToRefresh);
    }
}
