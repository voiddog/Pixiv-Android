package org.voiddog.pixiv.data.model.user;

import com.google.gson.annotations.SerializedName;

/**
 * Created by qigengxin on 16/9/1.
 */
public class ProfileModel {

    @SerializedName("webpage")
    private Object webpage;

    @SerializedName("gender")
    private String gender;

    @SerializedName("birth")
    private String birth;

    @SerializedName("region")
    private String region;

    @SerializedName("job")
    private String job;

    @SerializedName("total_follow_users")
    private int totalFollowUsers;

    @SerializedName("total_follower")
    private int totalFollower;

    @SerializedName("total_mypixiv_users")
    private int totalMypixivUsers;

    @SerializedName("total_illusts")
    private int totalIllusts;

    @SerializedName("total_manga")
    private int totalManga;

    @SerializedName("total_novels")
    private int totalNovels;

    @SerializedName("total_illust_bookmarks_public")
    private int totalIllustBookmarksPublic;

    @SerializedName("background_image_url")
    private Object backgroundImageUrl;

    @SerializedName("twitter_account")
    private String twitterAccount;

    @SerializedName("twitter_url")
    private Object twitterUrl;

    @SerializedName("is_premium")
    private Boolean isPremium;
}
