package org.voiddog.pixiv.domain;

import android.content.Context;

import org.voiddog.lib.net.NetConfiguration;
import org.voiddog.lib.net.NetCore;
import org.voiddog.pixiv.data.api.IllustsApi;
import org.voiddog.pixiv.data.api.UserApi;
import org.voiddog.pixiv.data.model.user.AccountModel;
import org.voiddog.pixiv.data.request.AccessTokenRequest;
import org.voiddog.pixiv.domain.anno.Logined;
import org.voiddog.pixiv.domain.interceptor.LoginHeaderInterceptor;
import org.voiddog.pixiv.domain.manager.AccountManager;
import org.voiddog.pixiv.domain.manager.BookmarkManager;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * 数据层管理核心
 * Created by qigengxin on 16/9/1.
 */
public class DataCoreImpl implements IDataCore {
    private static final String APP_HOST = "https://app-api.pixiv.net";

    AccountManager accountManager;
    BookmarkManager bookmarkManager;
    PixivNetCore mNetCore;

    public DataCoreImpl(Context context) {
        accountManager = new AccountManager(context);
        bookmarkManager = new BookmarkManager(context);

        createNetworkCore();
        if(isLogin()){
            uploadAddedBookmark();
        }
    }

    @Override
    public boolean isLogin() {
        return accountManager.isLogin();
    }

    @Override
    public void login(AccountModel accountModel) {
        accountManager.login(accountModel);
        createNetworkCore();
        uploadAddedBookmark();
    }

    @Override
    public void logout() {
        accountManager.logout();
        createNetworkCore();
    }

    @Override
    public IllustsApi getIllustsApi() {
        return mNetCore.createApi(IllustsApi.class);
    }

    private void createNetworkCore(){
        NetConfiguration.Builder builder = NetConfiguration.newBuilder();
        builder.setHost(APP_HOST);
        if(accountManager.isLogin()){
            builder.addExInterceptor(new LoginHeaderInterceptor(accountManager.getAccountModel().accessToken));
        }
        mNetCore = new PixivNetCore(builder.build());
    }

    private void uploadAddedBookmark(){
        List<String> addedBookmark = bookmarkManager.getAddedIllusts();
        Observable.from(addedBookmark)
                .flatMap(new Func1<String, Observable<Object>>() {
                    @Override
                    public Observable<Object> call(String s) {
                        return mNetCore.createApi(IllustsApi.class).addBookMark(
                                s, "public"
                        );
                    }
                })
                .subscribe();
        bookmarkManager.cleanIllustsBookmark();
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
                            return processNotLogin(method, args);
                        }
                        else if(logined != null
                                && accountManager.getAccountModel().isOutOfData()){
                            return processRefreshToken(accountManager.getAccountModel().refreshToken
                                    , (Observable) method.invoke(realProxy, args));
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

        Observable processRefreshToken(String refreshToken, final Observable realObserver){
            UserApi userApi = super.createApi(UserApi.class);
            AccessTokenRequest request = new AccessTokenRequest();
            request.grantType = AccessTokenRequest.REFRESH_TOKEN_TYPE;
            request.refreshToken = refreshToken;
            return userApi.getAccessToken(request)
                    .flatMap(new Func1<AccountModel, Observable<?>>() {
                        @Override
                        public Observable<?> call(AccountModel accountModel) {
                            accountManager.updateAccount(accountModel);
                            return realObserver;
                        }
                    });
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
