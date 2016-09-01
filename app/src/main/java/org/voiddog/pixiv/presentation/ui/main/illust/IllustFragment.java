package org.voiddog.pixiv.presentation.ui.main.illust;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import org.voiddog.lib.ui.DrawableItemDecoration;
import org.voiddog.lib.util.SizeUtil;
import org.voiddog.pixiv.PixivApplication;
import org.voiddog.pixiv.data.model.illusts.IllustsRankingModel;
import org.voiddog.pixiv.presentation.ui.common.fragment.lce.LceRecycleFragment;

/**
 *
 * Created by qigengxin on 16/8/26.
 */
public class IllustFragment extends LceRecycleFragment<IllustsRankingModel, IllustView, IllustPresenter>
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
        DrawableItemDecoration itemDecoration = new DrawableItemDecoration(
                new ColorDrawable(0x00ffffff), DrawableItemDecoration.VERTICAL | DrawableItemDecoration.HORIZONTAL
        );
        itemDecoration.setHeight(SizeUtil.dp2px(view.getContext(), 2));
        itemDecoration.setWidth(SizeUtil.dp2px(view.getContext(), 2));
        mRvContent.addItemDecoration(itemDecoration);
        loadData(true);
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return "网络错误";
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        getPresenter().loadData(pullToRefresh);
    }
}
