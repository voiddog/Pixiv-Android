package org.voiddog.pixiv.presentation.ui.main;

import android.support.v4.app.Fragment;

import org.voiddog.lib.mvp.MvpView;
import org.voiddog.pixiv.presentation.ui.common.activity.base.BaseActivityMvpView;

/**
 * Created by qigengxin on 16/8/25.
 */
public interface MainView extends BaseActivityMvpView {

    void setTabLayout(String[] titles);

    void setContentFragment(Fragment fragment, int index);
}
