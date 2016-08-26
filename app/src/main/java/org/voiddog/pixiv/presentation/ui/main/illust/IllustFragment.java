package org.voiddog.pixiv.presentation.ui.main.illust;

import org.voiddog.pixiv.data.model.RankingModel;
import org.voiddog.pixiv.presentation.ui.common.fragment.lce.LceRecycleFragment;

import rx.Observable;

/**
 *
 * Created by qigengxin on 16/8/26.
 */
public class IllustFragment extends LceRecycleFragment<RankingModel, IllustView, IllustPresenter>{

    @Override
    public Observable<RankingModel> getRequestObservable(boolean reload) {
        return null;
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return null;
    }

}
