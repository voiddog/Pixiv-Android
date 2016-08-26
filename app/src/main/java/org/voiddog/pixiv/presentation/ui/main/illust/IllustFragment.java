package org.voiddog.pixiv.presentation.ui.main.illust;

import android.os.Bundle;

import org.voiddog.pixiv.data.model.RankingModel;
import org.voiddog.pixiv.presentation.ui.common.fragment.lce.LceRecycleFragment;

/**
 *
 * Created by qigengxin on 16/8/26.
 */
public class IllustFragment extends LceRecycleFragment<RankingModel, IllustView, IllustPresenter>{

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return null;
    }

    @Override
    public void loadData(boolean pullToRefresh) {

    }
}
