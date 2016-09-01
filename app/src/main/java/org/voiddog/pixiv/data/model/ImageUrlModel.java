package org.voiddog.pixiv.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * 图片链接
 * Created by qigengxin on 16/8/26.
 */
public class ImageUrlModel {
    @SerializedName(value = "square_medium", alternate = {"px_16x16"})
    public String squareMedium;
    @SerializedName(value = "medium", alternate = {"px_50x50"})
    public String medium;
    @SerializedName(value = "large", alternate = {"px_170x170"})
    public String large;

    public String original;

}
