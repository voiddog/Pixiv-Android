package org.voiddog.lib.net.intercept;


import org.voiddog.lib.util.LogUtil;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.Platform;
import okio.Buffer;
import okio.BufferedSource;

/**
 * 日志输出器
 * Created by qgx44 on 2016/8/28.
 */
public class LoggerIntercept implements Interceptor {
    static final String TAG = "NetworkResponse";
    private final static Charset UTF8 = Charset.forName("UTF-8");

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        LogUtil.I(TAG, "Request:");
        LogUtil.I(TAG, "<-----------Head------------>");
        for(String name : request.headers().names()){
            LogUtil.I(TAG, name + ":\t" + request.headers().get(name));
        }
        Response response = chain.proceed(chain.request());
        LogUtil.I(TAG, "<----------Response--------->");
        LogUtil.I(TAG, "Status code:\t" + response.code());

        try {
            BufferedSource source = response.body().source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();
            byte[] bytes = buffer.clone().readByteArray();
            LogUtil.I(TAG, "Body:\t");
            Platform.get().log(Platform.INFO, new String(bytes, UTF8), null);
        }
        catch (Exception ignore){
            LogUtil.I(TAG, "not support");
        }
        return response;
    }
}
