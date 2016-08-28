package org.voiddog.pixiv.data.api;

import org.voiddog.pixiv.data.model.RankingModel;

import retrofit2.http.GET;
import retrofit2.http.Path;
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

    @GET("{next}")
    Observable<RankingModel> next(@Path("next") String nextUrl);
}
