package org.voiddog.pixiv.domain;


import org.voiddog.lib.net.NetConfiguration;
import org.voiddog.lib.net.NetCore;
import org.voiddog.pixiv.data.api.IllustsApi;
import org.voiddog.pixiv.domain.interceptor.LoginHeaderInterceptor;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;

/**
 * Retrofit管理类
 * Created by qigengxin on 16/8/26.
 */
public class ApiHelper {
    private static final String APP_HOST = "https://app-api.pixiv.net";
    private static final String AUTH_HOST = "https://oauth.secure.pixiv.net";

    private NetCore mAppNetCore;
    private Map<Class, Object> mApiCache = new HashMap<>();

    public ApiHelper(){}

    public void login(String token){
        mApiCache.clear();
        mAppNetCore = new NetCore(NetConfiguration.newBuilder()
                .setDebug(true)
                .setHost(APP_HOST)
                .addExInterceptor(new LoginHeaderInterceptor(token))
                .build());
    }

    public IllustsApi getIllutsApi(){
        Object ret = mApiCache.get(IllustsApi.class);
        if(ret == null){
            ret = getAppRetrofit().create(IllustsApi.class);
            mApiCache.put(IllustsApi.class, ret);
        }
        return (IllustsApi) ret;
    }

    private Retrofit getAppRetrofit(){
        if(mAppNetCore == null){
            mAppNetCore = new NetCore(NetConfiguration.newBuilder()
                    .setHost(APP_HOST)
                    .setDebug(true)
                    .build());
            mApiCache.clear();
        }
        return mAppNetCore.getRetrofit();
    }

    private Retrofit getAuthRetrofit(){
        return (new NetCore(NetConfiguration.newBuilder()
                .setHost(AUTH_HOST)
                .setDebug(true)
                .build())).getRetrofit();
    }
}
