package org.voiddog.pixiv;

import android.content.Context;

import org.voiddog.pixiv.domain.ApiManager;
import org.voiddog.pixiv.domain.BookmarkManager;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by qigengxin on 16/8/25.
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    Context getContext();

    ApiManager getApiManager();

    BookmarkManager getBookmarkManager();
}
