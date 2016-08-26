package org.voiddog.pixiv.presentation.ui.main.illust;

import android.content.Context;

import org.voiddog.pixiv.presentation.ui.common.fragment.base.FragmentScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by qigengxin on 16/8/26.
 */
@Module
public class IllustModule {

    Context mContext;

    public IllustModule(Context context){
        mContext = context;
    }

    @Provides
    @FragmentScope
    IllustPresenter providePresenter(Context context){
        return new IllustPresenter(context);
    }
}
