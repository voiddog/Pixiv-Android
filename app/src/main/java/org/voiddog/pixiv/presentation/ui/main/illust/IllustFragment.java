package org.voiddog.pixiv.presentation.ui.main.illust;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.voiddog.pixiv.PixivApplication;
import org.voiddog.pixiv.data.model.RankingModel;
import org.voiddog.pixiv.presentation.ui.common.fragment.lce.LceRecycleFragment;

/**
 *
 * Created by qigengxin on 16/8/26.
 */
public class IllustFragment extends LceRecycleFragment<RankingModel, IllustView, IllustPresenter>
        implements IllustView{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        DaggerIllustComponent.builder()
                .appComponent(PixivApplication.getInstance().getAppComponent())
                .illustModule(new IllustModule(getActivity()))
                .build()
                .inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData(true);
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return e.getMessage();
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        getPresenter().loadData(pullToRefresh);
    }
}
