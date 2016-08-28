package org.voiddog.pixiv.presentation.ui.main;

import android.content.Context;
import android.os.Bundle;

import org.voiddog.lib.mvp.MvpBasePresenter;
import org.voiddog.pixiv.R;
import org.voiddog.pixiv.presentation.ui.common.activity.base.ForActivity;
import org.voiddog.pixiv.presentation.ui.main.illust.IllustFragment;

import javax.inject.Inject;

/**
 * Created by qigengxin on 16/8/25.
 */
public class MainPresenter extends MvpBasePresenter<MainView> {

    @Inject
    @ForActivity
    Context mContext;

    @Inject
    IllustFragment mIllFragment;

    public void init(Bundle saveInstance){
        if(!isViewAttached()){
            return;
        }

        getView().setTabLayout(new String[]{
                mContext.getResources().getString(R.string.main_tab_illusts),
                mContext.getResources().getString(R.string.main_tab_manga)
        });

        // 默认显示第一页
        getView().setContentFragment(mIllFragment, 1);
    }
}
