package org.voiddog.pixiv.data.api;

import org.voiddog.pixiv.data.model.user.AccountModel;
import org.voiddog.pixiv.data.request.AccessTokenRequest;

import retrofit2.http.Body;
import retrofit2.http.GET;
import rx.Observable;

/**
 * 用户接口
 * Created by qgx44 on 2016/9/1.
 */
public interface UserApi {
    @GET("https://oauth.secure.pixiv.net/auth/token")
    Observable<AccountModel> getAccessToken(@Body AccessTokenRequest request);
}
