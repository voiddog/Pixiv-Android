package org.voiddog.pixiv.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 插画列表
 * Created by qigengxin on 16/8/26.
 */
public class IllustsModel {

    public long id;
    // 标题
    public String title;
    // 类型 illust or manga
    public String type;

    @SerializedName("image_urls")
    public ImageUrlModel imageUrls;
    // 描述
    public String caption;
    // 限制 ?
    public int restrict;
    // 标签列表
    public List<TagModel> tags;
    // 使用什么工具画的
    public List<String> tools;
    // 创建时间
    @SerializedName("create_data")
    public String createData;
    // 图片数量 多图 > 1
    @SerializedName("page_count")
    public int pageCount;
    // 图片信息
    public int width;

    public int height;
    // 不知道什么鬼
    @SerializedName("sanity_level")
    public int sanityLevel;
    // 内有原图 单张
    @SerializedName("meta_single_page")
    public PageModel metaSinglePage;
    // 多图 多张
    @SerializedName("meta_pages")
    public List<PageModel> metaPages;
    // 总浏览量
    @SerializedName("total_view")
    public long totalView;
    // 总标记数
    @SerializedName("total_bookmarks")
    public int totalBookmarks;
    // 是否被标记
    @SerializedName("is_bookmarked")
    public boolean isBookmarked;
    // 是够可见
    public boolean visible;
}
