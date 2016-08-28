package org.voiddog.pixiv.presentation.ui.main.illust;

import org.voiddog.pixiv.AppComponent;
import org.voiddog.pixiv.presentation.ui.common.fragment.base.FragmentScope;

import dagger.Component;

/**
 * Created by qigengxin on 16/8/26.
 */
@FragmentScope
@Component(dependencies = AppComponent.class, modules = {IllustModule.class})
public interface IllustComponent {

    void inject(IllustFragment fragment);

    void inject(IllustPresenter presenter);
}
