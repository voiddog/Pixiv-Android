package org.voiddog.pixiv.domain;

import android.content.Context;

import org.voiddog.lib.net.NetConfiguration;
import org.voiddog.lib.net.NetCore;
import org.voiddog.pixiv.data.api.IllustsApi;
import org.voiddog.pixiv.data.model.user.AccountModel;
import org.voiddog.pixiv.domain.anno.Logined;
import org.voiddog.pixiv.domain.interceptor.LoginHeaderInterceptor;
import org.voiddog.pixiv.domain.manager.AccountManager;
import org.voiddog.pixiv.domain.manager.BookmarkManager;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import rx.Observable;
import rx.Subscriber;

/**
 * 数据层管理核心
 * Created by qigengxin on 16/9/1.
 */
public class DataCoreImpl implements IDataCore {

    AccountManager accountManager;
    BookmarkManager bookmarkManager;
    PixivNetCore mNetCore;
    Map<Class, Object> mApiCache = new HashMap<>();

    public DataCoreImpl(Context context) {
        accountManager = new AccountManager(context);
        bookmarkManager = new BookmarkManager(context);

        NetConfiguration.Builder builder = NetConfiguration.newBuilder();
        builder.setHost("https://app-api.pixiv.net");
        if(accountManager.isLogin()){
            builder.addExInterceptor(new LoginHeaderInterceptor(accountManager.getAccountModel().accessToken));
        }
        mNetCore = new PixivNetCore(builder.build());
    }

    @Override
    public boolean isLogin() {
        return accountManager.isLogin();
    }

    @Override
    public void login(AccountModel accountModel) {
        accountManager.login(accountModel);
    }

    @Override
    public void logout() {
        accountManager.logout();
    }

    @Override
    public IllustsApi getIllustsApi() {
        Object ret = mApiCache.get(IllustsApi.class);
        if(ret == null){
            ret = mNetCore.createApi(IllustsApi.class);
            mApiCache.put(IllustsApi.class, ret);
        }
        return (IllustsApi) ret;
    }

    class PixivNetCore extends NetCore {

        public PixivNetCore(NetConfiguration configuration) {
            super(configuration);
        }

        private Map<Class<?>, Object> serviceMap = new ConcurrentHashMap<>();

        @Override
        public <T> T createApi(final Class<T> clazz) {
            Object o = serviceMap.get(clazz);
            if(o == null){
                final T realProxy = super.createApi(clazz);
                o = Proxy.newProxyInstance(DataCoreImpl.class.getClassLoader(), new Class[]{clazz}, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Logined logined = method.getAnnotation(Logined.class);
                        if(logined != null && !isLogin()){
                            processNotLogin(method, args);
                        }
                        return method.invoke(realProxy, args);
                    }
                });
                serviceMap.put(clazz, o);
            }
            return (T) o;
        }

        Observable processNotLogin(Method method, Object[] args){
            if(method.getName().equals("addBookMark")){
                String id = (String) args[0];
                bookmarkManager.putIllustsBookmark(id, true);
                return getCompleteOvservable();
            }
            else if (method.getName().equals("deleteBookMark")){
                String id = (String) args[0];
                bookmarkManager.putIllustsBookmark(id, false);
                return getCompleteOvservable();
            }
            return getErrorOvserable();
        }

        Observable getCompleteOvservable(){
            return Observable.create(new Observable.OnSubscribe<Object>() {
                @Override
                public void call(Subscriber<? super Object> subscriber) {
                    subscriber.onCompleted();
                }
            });
        }

        Observable getErrorOvserable(){
            return Observable.create(new Observable.OnSubscribe<Object>() {
                @Override
                public void call(Subscriber<? super Object> subscriber) {
                    subscriber.onError(new IllegalArgumentException("需要登录"));
                }
            });
        }
    }
}
