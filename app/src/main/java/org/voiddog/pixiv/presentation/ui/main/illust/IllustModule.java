package org.voiddog.pixiv.presentation.ui.main.illust;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import org.voiddog.pixiv.data.api.IllustsApi;
import org.voiddog.pixiv.data.model.RankingModel;
import org.voiddog.pixiv.domain.ApiManager;
import org.voiddog.pixiv.presentation.ui.common.activity.base.ForActivity;
import org.voiddog.pixiv.presentation.ui.common.fragment.base.FragmentScope;
import org.voiddog.pixiv.presentation.ui.common.fragment.lce.ILceDataHelper;
import org.voiddog.pixiv.presentation.ui.main.illust.adapter.IllustAdapter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by qigengxin on 16/8/26.
 */
@Module
public class IllustModule {

    Context mContext;

    public IllustModule(Context context){
        mContext = context;
    }

    @Provides
    @ForActivity
    Context provideActivityContext(){
        return mContext;
    }

    @Provides
    @FragmentScope
    IllustsApi provideIllustsApi(ApiManager helper){
        return helper.getIllutsApi();
    }

    @Provides
    @FragmentScope
    IllustPresenter providePresenter(IllustComponent component){
        IllustPresenter presenter = new IllustPresenter();
        component.inject(presenter);
        return presenter;
    }

    @Provides
    @FragmentScope
    IllustAdapter provideIllAdapter(){
        return new IllustAdapter();
    }

    @Provides
    @FragmentScope
    RecyclerView.Adapter provideAdapter(IllustAdapter adapter){
        return adapter;
    }

    @Provides
    @FragmentScope
    ILceDataHelper<RankingModel> provideLceDataHelper(IllustAdapter adapter){
        return adapter;
    }

    @Provides
    RecyclerView.LayoutManager provideLayoutManager(){
        return new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    }
}
