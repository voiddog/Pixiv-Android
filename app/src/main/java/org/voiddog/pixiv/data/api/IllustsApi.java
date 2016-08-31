package org.voiddog.pixiv.data.api;

import org.voiddog.pixiv.data.model.RankingModel;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

/**
 * 插画 Api
 * Created by qigengxin on 16/8/26.
 */
public interface IllustsApi {

    @GET("v1/illust/recommended-nologin?include_ranking_label=true")
    Observable<RankingModel> rankListWithoutLogin();

    @GET("v1/illust/recommended?content_type=illust&include_ranking_label=true")
    Observable<RankingModel> rankList();

    @GET
    Observable<RankingModel> next(@Url String nextUrl);

    @FormUrlEncoded
    @POST("v1/illust/bookmark/add")
    Observable<Object> addBookMark(@Field("illust_id") String illustId, @Field("restrict") String restrict);

    @FormUrlEncoded
    @POST("v1/illust/bookmark/delete")
    Observable<Object> deleteBookMark(@Field("illust_id") String illustId);
}
