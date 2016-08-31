package org.voiddog.pixiv.domain;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

/**
 * 书签缓存管理
 * Created by qigengxin on 16/8/31.
 */
public class BookmarkManager {
    private static final String ILLUSTS_BOOKMARK_CACHE = "illusts_bookmark_cache";

    private SharedPreferences mIllusts;

    public BookmarkManager(Context context){
        mIllusts = context.getSharedPreferences(ILLUSTS_BOOKMARK_CACHE, Context.MODE_PRIVATE);
    }

    public List<String> getAddedIllusts(){
        List<String> illusts = new ArrayList<>();
        for(String key : mIllusts.getAll().keySet()){
            if(mIllusts.getBoolean(key, false)){
                illusts.add(key);
            }
        }
        return illusts;
    }

    public void putIllustsBookmark(String id, boolean isAdd){
        mIllusts.edit()
                .putBoolean(id, isAdd)
                .apply();
    }

    public void cleanIllustsBookmark(){
        mIllusts.edit()
                .clear().apply();
    }
}
