package org.voiddog.pixiv.presentation.ui.main;

import org.voiddog.pixiv.presentation.ui.common.activity.base.ActivityModule;
import org.voiddog.pixiv.presentation.ui.common.activity.base.ActivityScope;

import dagger.Component;

/**
 * Created by qigengxin on 16/8/26.
 */
@ActivityScope
@Component(modules = {MainModule.class, ActivityModule.class})
public interface MainComponent {

    void inject(MainActivity activity);

    void inject(MainPresenter presenter);

    MainPresenter getPresenter();
}
