package org.voiddog.pixiv;

import android.content.Context;

import org.voiddog.pixiv.domain.IDataCore;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by qigengxin on 16/8/25.
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    Context getContext();

    IDataCore getIDataCore();
}
