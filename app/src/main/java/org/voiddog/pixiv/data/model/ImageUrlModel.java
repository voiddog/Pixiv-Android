package org.voiddog.pixiv.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * 图片链接
 * Created by qigengxin on 16/8/26.
 */
public class ImageUrlModel {
    @SerializedName("square_medium")
    public String squareMedium;

    public String medium;

    public String large;

    public String original;

}
