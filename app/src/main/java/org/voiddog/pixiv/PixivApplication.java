package org.voiddog.pixiv;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

import org.voiddog.lib.BaseApplication;
import org.voiddog.lib.util.LogUtil;
import org.voiddog.pixiv.domain.interceptor.ReferHeaderIntercept;

import okhttp3.OkHttpClient;

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

        // Debug
        LogUtil.debug = true;

        // 初始化 fresco
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new ReferHeaderIntercept())
                .build();
        ImagePipelineConfig config = OkHttpImagePipelineConfigFactory
                .newBuilder(this, okHttpClient)
                .setDownsampleEnabled(true)
                .build();
        Fresco.initialize(this, config);

        // 初始化AppComponent
        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent(){
        return mAppComponent;
    }
}
