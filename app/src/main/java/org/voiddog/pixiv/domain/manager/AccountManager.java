package org.voiddog.pixiv.domain.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import org.voiddog.pixiv.data.model.user.AccountModel;

/**
 * 账号管理器
 * Created by qigengxin on 16/9/1.
 */
public class AccountManager {
    private static final String ACCOUNT_PREFERENCE_PATH = "account_path";

    private static final String ACCOUNT_NAME = "account_preference";

    private SharedPreferences mSp;

    private AccountModel mAccountModel;

    private Gson mGson = new Gson();

    public AccountManager(Context context){
        mSp = context.getSharedPreferences(ACCOUNT_PREFERENCE_PATH, Context.MODE_PRIVATE);
        String json = mSp.getString(ACCOUNT_NAME, null);
        if(json != null){
            mAccountModel = mGson.fromJson(json, AccountModel.class);
        }
    }

    public void login(AccountModel accountModel){
        updateAccount(accountModel);
    }

    public void logout(){
        mAccountModel = null;
        mSp.edit().clear().apply();
    }

    public void updateAccount(AccountModel accountModel){
        mAccountModel = accountModel;
        mSp.edit().putString(
                ACCOUNT_NAME, mGson.toJson(mAccountModel)
        ).apply();
    }

    public AccountModel getAccountModel(){
        return mAccountModel;
    }

    public boolean isLogin(){
        return mAccountModel != null;
    }
}
