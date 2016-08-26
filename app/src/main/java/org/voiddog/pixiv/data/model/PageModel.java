package org.voiddog.pixiv.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by qigengxin on 16/8/26.
 */
public class PageModel {
    // 单张图
    @SerializedName("original_image_url")
    public String originalImageUrl;
    // 多张图
    @SerializedName("image_urls")
    public ImageUrlModel imageUrls;
}
