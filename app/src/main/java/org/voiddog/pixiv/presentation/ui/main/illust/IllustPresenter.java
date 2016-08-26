package org.voiddog.pixiv.presentation.ui.main.illust;

import android.content.Context;

import org.voiddog.lib.mvp.lce.MvpLceRxPresenter;
import org.voiddog.pixiv.data.model.RankingModel;

/**
 * Created by qigengxin on 16/8/26.
 */
public class IllustPresenter extends MvpLceRxPresenter<RankingModel, IllustView>{

    private Context mContext;

    public IllustPresenter(Context context){
        mContext = context;
    }

}
