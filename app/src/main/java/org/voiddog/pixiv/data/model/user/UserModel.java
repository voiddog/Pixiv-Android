package org.voiddog.pixiv.data.model.user;

import com.google.gson.annotations.SerializedName;

import org.voiddog.pixiv.data.model.ImageUrlModel;

/**
 * 用户数据
 * Created by qigengxin on 16/8/26.
 */
public class UserModel {

    public long id;
    // 名称
    public String name;
    // 账号
    public String account;
    // 个人简要画廊信息
    @SerializedName("profile_image_urls")
    public ImageUrlModel profileImageUrls;
    // 是否被我关注
    @SerializedName("is_followed")
    public boolean isFollowed;

    public String comment;
}
