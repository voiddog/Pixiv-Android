package org.voiddog.pixiv.domain.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by qigengxin on 16/8/26.
 */
public class LoginHeaderInterceptor implements Interceptor{

    private String mToken;

    public LoginHeaderInterceptor(String bearerToken){
        mToken = bearerToken;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        request = request.newBuilder()
                .addHeader("Authorization", "Bearer " + mToken)
                .build();
        return chain.proceed(request);
    }
}
