package org.voiddog.lib.net.intercept;

import org.voiddog.lib.util.LogUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 日志输出器
 * Created by qgx44 on 2016/8/28.
 */
public class LoggerIntercept implements Interceptor {
    static final String TAG = "NetworkResponse";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        LogUtil.I(TAG, "Request: --------------------");
        LogUtil.I(TAG, "<-----------Head------------>");
        for(String name : request.headers().names()){
            LogUtil.I(TAG, name + ":\t" + request.headers().get(name));
        }
        Response response = chain.proceed(chain.request());
        LogUtil.I(TAG, "<----------Response--------->");
        LogUtil.I(TAG, "Status code:\t" + response.code());
        LogUtil.I(TAG, "Body:\t" + response.body().toString());
        return response;
    }
}
