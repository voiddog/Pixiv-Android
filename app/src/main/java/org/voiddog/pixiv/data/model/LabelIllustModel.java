package org.voiddog.pixiv.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by qigengxin on 16/8/26.
 */
public class LabelIllustModel {

    public String title;

    @SerializedName("user_name")
    public String userName;

    public int width;

    public int height;

    @SerializedName("image_urls")
    public ImageUrlModel imageUrls;
}
