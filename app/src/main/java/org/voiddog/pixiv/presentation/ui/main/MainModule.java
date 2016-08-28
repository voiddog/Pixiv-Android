package org.voiddog.pixiv.presentation.ui.main;

import org.voiddog.pixiv.presentation.ui.common.activity.base.ActivityScope;
import org.voiddog.pixiv.presentation.ui.main.illust.IllustFragment;

import dagger.Module;
import dagger.Provides;

/**
 * 主页模块
 * Created by qigengxin on 16/8/26.
 */
@Module
public class MainModule {

    @Provides
    IllustFragment provideIllustFragment(){
        return new IllustFragment();
    }

    @ActivityScope
    @Provides
    public MainPresenter provideMainPresenter(MainComponent component){
        MainPresenter presenter = new MainPresenter();
        component.inject(presenter);
        return presenter;
    }
}
