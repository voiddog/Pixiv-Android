package org.voiddog.pixiv;

import com.facebook.drawee.backends.pipeline.Fresco;

import org.voiddog.lib.BaseApplication;

/**
 * Created by qigengxin on 16/8/25.
 */
public class PixivApplication extends BaseApplication{
    static PixivApplication sInstance;

    public static PixivApplication getInstance(){
        return sInstance;
    }

    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        // 初始化 fresco
        Fresco.initialize(this);
        // 初始化网络
    }

    public AppComponent getAppComponent(){
        return mAppComponent;
    }
}
