package org.voiddog.pixiv.domain;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 *
 * Created by qigengxin on 16/9/1.
 */
@Module
public class DataModel {

    private Context mContext;

    public DataModel(Context context){
        mContext = context;
    }

    @Provides
    @Singleton
    IDataCore provideIDataCore(){
        return new DataCoreImpl(mContext);
    }
}
