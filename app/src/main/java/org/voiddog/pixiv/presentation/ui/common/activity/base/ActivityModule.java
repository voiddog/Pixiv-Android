package org.voiddog.pixiv.presentation.ui.common.activity.base;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Created by qigengxin on 16/8/26.
 */
@Module
public class ActivityModule {

    private Activity mActivity;

    public ActivityModule(Activity activity){
        mActivity = activity;
    }

    @ActivityScope
    @ForActivity
    @Provides
    public Context provideContext(){
        return mActivity;
    }

    @ActivityScope
    @Provides
    public Activity provideActivity(){
        return mActivity;
    }
}
