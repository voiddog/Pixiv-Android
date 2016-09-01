package org.voiddog.pixiv;

import android.app.Application;
import android.content.Context;

import org.voiddog.pixiv.domain.DataCoreImpl;
import org.voiddog.pixiv.domain.IDataCore;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by qigengxin on 16/8/25.
 */
@Module
public class AppModule {

    Application mApplication;

    public AppModule(Application application){
        mApplication = application;
    }

    @Provides
    @Singleton
    public Context provideContext(){
        return mApplication;
    }

    @Provides
    @Singleton
    public IDataCore provideDataCore(Context context){
        return new DataCoreImpl(context);
    }
}
