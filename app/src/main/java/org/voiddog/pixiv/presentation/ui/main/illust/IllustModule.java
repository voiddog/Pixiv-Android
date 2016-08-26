package org.voiddog.pixiv.presentation.ui.main.illust;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import org.voiddog.pixiv.data.model.RankingModel;
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
    @FragmentScope
    IllustPresenter providePresenter(Context context){
        return new IllustPresenter(context);
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
}
