package org.voiddog.lib.net;

import android.annotation.SuppressLint;

import org.voiddog.lib.net.intercept.LoggerIntercept;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络核心租金
 * Created by qigengxin on 16/8/24.
 */
public class NetCore {

    Retrofit mRetrofit;
    OkHttpClient mOkHttpClient;
    Map<Class, Object> mServiceCache = new ConcurrentHashMap<>();

    public NetCore(NetConfiguration configuration){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        if(configuration.getExInterceptor() != null) {
            for (Interceptor interceptor : configuration.getExInterceptor()) {
                builder.addInterceptor(interceptor);
            }
        }

        // 超时设置
        builder.readTimeout(configuration.getReadTimeout(), TimeUnit.MILLISECONDS);
        builder.connectTimeout(configuration.getConnectionTimeout(), TimeUnit.MILLISECONDS);
        builder.writeTimeout(configuration.getWriteTimeout(), TimeUnit.MILLISECONDS);

        if(configuration.isDebug()){
            builder.sslSocketFactory(createSSLSocketFactory(), new TrustAllManager());
            builder.hostnameVerifier(new TrustAllHostVerifier());
            builder.addInterceptor(new LoggerIntercept());
        }

        mOkHttpClient = builder.build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(configuration.getHost())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(mOkHttpClient)
                .build();
    }

    public Retrofit getRetrofit(){
        if(mRetrofit == null){
            throw new IllegalArgumentException("未初始化");
        }
        return mRetrofit;
    }

    public OkHttpClient getOkHttpClient(){
        if(mOkHttpClient == null){
            throw new IllegalArgumentException("未初始化");
        }
        return mOkHttpClient;
    }

    public <T> T createApi(final Class<T> clazz){
        Object o = mServiceCache.get(clazz);
        if(o == null){
            o = mRetrofit.create(clazz);
            mServiceCache.put(clazz, o);
        }
        return (T) o;
    }

    /**
     * 默认信任所有的证书
     *
     * @return
     */
    @SuppressLint("TrulyRandom")
    private static SSLSocketFactory createSSLSocketFactory() {

        SSLSocketFactory sSLSocketFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllManager()},
                    new SecureRandom());
            sSLSocketFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return sSLSocketFactory;
    }

    private static class TrustAllManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    /**
     * 信任所有host
     */
    private static class TrustAllHostVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
}