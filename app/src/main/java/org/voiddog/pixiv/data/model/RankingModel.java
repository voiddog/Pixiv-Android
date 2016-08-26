package org.voiddog.pixiv.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 推荐列表数据
 * Created by qigengxin on 16/8/26.
 */
public class RankingModel {

    public List<IllustsModel> illusts;
    // 下一页数据
    @SerializedName("next_url")
    public String nextUrl;
    // 暂时不知
    @SerializedName("home_ranking_illusts")
    public List<IllustsModel> homeRankingIllusts;

    @SerializedName("ranking_label_illust")
    public LabelIllustModel rankingLabelIllust;
}
