package org.voiddog.pixiv.domain.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 添加图片请求用的Refer
 * Created by qgx44 on 2016/8/28.
 */
public class ReferHeaderIntercept implements Interceptor{
    static final String IMAGE_REFERER = "https://app-api.pixiv.net/";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        request = request.newBuilder()
                .addHeader("referer", IMAGE_REFERER)
                .build();
        return chain.proceed(request);
    }
}
