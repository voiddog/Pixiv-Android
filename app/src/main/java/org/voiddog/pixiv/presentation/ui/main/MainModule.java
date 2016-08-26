package org.voiddog.pixiv.presentation.ui.main;

import org.voiddog.pixiv.presentation.ui.common.activity.base.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * 主页模块
 * Created by qigengxin on 16/8/26.
 */
@Module
public class MainModule {

    @ActivityScope
    @Provides
    public MainPresenter provideMainPresenter(MainComponent component){
        return new MainPresenter(component);
    }
}
