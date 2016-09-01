package org.voiddog.pixiv.data.model.user;

import com.google.gson.annotations.SerializedName;

/**
 * 账号信息
 * Created by qigengxin on 16/9/1.
 */
public class AccountModel {

    public String accessToken;

    public String refreshToken;
    // 到期时间
    @SerializedName("expires_in")
    public long expiresIn;

    public UserModel userModel;

    // 当前更新expiresIn的时间
    private long mUpdateExpiresTime;

    public AccountModel(){
        mUpdateExpiresTime = System.currentTimeMillis();
    }

    public boolean isOutOfData(){
        return (System.currentTimeMillis() - mUpdateExpiresTime) / 1000 > expiresIn;
    }
}
