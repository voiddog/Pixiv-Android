package org.voiddog.pixiv.presentation.ui.common.activity.base;

import org.voiddog.lib.mvp.MvpActivity;
import org.voiddog.lib.mvp.MvpPresenter;
import org.voiddog.lib.mvp.MvpView;


/**
 * 基础MvpActivity
 * Created by qigengxin on 16/8/25.
 */
public abstract class BaseMvpActivity<V extends MvpView, P extends MvpPresenter<V>>
        extends MvpActivity<V, P>{


}
