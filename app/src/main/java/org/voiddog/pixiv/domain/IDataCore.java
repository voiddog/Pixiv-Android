package org.voiddog.pixiv.domain;

import org.voiddog.pixiv.data.api.IllustsApi;
import org.voiddog.pixiv.data.model.user.AccountModel;

/**
 * 数据层接口
 * Created by qigengxin on 16/9/1.
 */
public interface IDataCore {
    boolean isLogin();

    /**
     * 登录
     *
     * @param accountModel
     */
    void login(AccountModel accountModel);

    /**
     * 登出
     */
    void logout();

    /**
     * 获取 插画相关 api
     *
     * @return
     */
    IllustsApi getIllustsApi();
}
