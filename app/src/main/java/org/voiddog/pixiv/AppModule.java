package org.voiddog.pixiv;

import android.app.Application;
import android.content.Context;

import org.voiddog.pixiv.domain.ApiManager;
import org.voiddog.pixiv.domain.BookmarkManager;

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
    public ApiManager provideApiHelper(){
        return new ApiManager();
    }

    @Provides
    @Singleton
    public BookmarkManager provideBookmarkHelper(Context context){
        return new BookmarkManager(context);
    }
}
