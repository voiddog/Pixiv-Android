package org.voiddog.lib.net;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;

/**
 * Created by qigengxin on 16/8/24.
 */
public class NetConfiguration {
    /**
     * 主host
     */
    private String mHost;
    /**
     * 是否输出日志
     */
    private boolean mDebug;
    /**
     * 是否使用 gzip 压缩
     */
    private boolean mGzip;
    /**
     * 扩展拦截器
     */
    private List<Interceptor> mExInterceptor = null;
    /**
     * 超时时间
     */
    private int mConnectionTimeout;
    private int mReadTimeout;
    private int mWriteTimeout;

    private NetConfiguration(Builder builder){
        mDebug = builder.isDebug();
        mGzip = builder.isGzip();
        mExInterceptor = builder.getExInterceptor();
        mConnectionTimeout = builder.getConnectionTimeout();
        mReadTimeout = builder.getReadTimeout();
        mWriteTimeout = builder.getWriteTimeout();
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public boolean isDebug() {
        return mDebug;
    }

    public boolean isGzip() {
        return mGzip;
    }

    public List<Interceptor> getExInterceptor() {
        return mExInterceptor;
    }

    public int getConnectionTimeout() {
        return mConnectionTimeout;
    }

    public int getReadTimeout() {
        return mReadTimeout;
    }

    public int getWriteTimeout() {
        return mWriteTimeout;
    }

    public String getHost() {
        return mHost;
    }

    public static class Builder{
        private boolean mDebug = false, mGzip = true;
        private List<Interceptor> mExInterceptor = null;
        private int mConnectionTimeout = 100000;
        private int mReadTimeout = 100000;
        private int mWriteTimeout = 100000;
        private String mHost = null;

        public boolean isGzip() {
            return mGzip;
        }

        public Builder setGzip(boolean mGzip) {
            this.mGzip = mGzip;
            return this;
        }

        public List<Interceptor> getExInterceptor() {
            return mExInterceptor;
        }

        public Builder addExInterceptors(List<Interceptor> mExInterceptor) {
            if(mExInterceptor == null){
                mExInterceptor = new ArrayList<>();
            }
            this.mExInterceptor.addAll(mExInterceptor);
            return this;
        }

        public Builder addExInterceptor(Interceptor interceptor){
            if(mExInterceptor == null){
                mExInterceptor = new ArrayList<>();
            }
            mExInterceptor.add(interceptor);
            return this;
        }

        public int getConnectionTimeout() {
            return mConnectionTimeout;
        }

        public Builder setConnectionTimeout(int mConnectionTimeout) {
            this.mConnectionTimeout = mConnectionTimeout;
            return this;
        }

        public int getReadTimeout() {
            return mReadTimeout;
        }

        public Builder setReadTimeout(int mReadTimeout) {
            this.mReadTimeout = mReadTimeout;
            return this;
        }

        public int getWriteTimeout() {
            return mWriteTimeout;
        }

        public Builder setWriteTimeout(int mWriteTimeout) {
            this.mWriteTimeout = mWriteTimeout;
            return this;
        }

        public boolean isDebug() {
            return mDebug;
        }

        public Builder setDebug(boolean mDebug) {
            this.mDebug = mDebug;
            return this;
        }

        public String getHost() {
            return mHost;
        }

        public Builder setHost(String mHost) {
            this.mHost = mHost;
            return this;
        }

        public NetConfiguration build(){
            return new NetConfiguration(this);
        }

        private void checkNull(){
            if(mHost == null){
                throw new IllegalArgumentException("host not set");
            }
        }
    }
}
