package org.voiddog.imageselector.ui;

import org.voiddog.imageselector.data.AlbumItemData;
import org.voiddog.lib.mvp.MvpView;

import java.util.List;

/**
 * 图片选择器视图
 * Created by qigengxin on 16/9/7.
 */
public interface SelectImgMvpView extends MvpView {

    void showError(String msg);

    void showImages(String title, List<String> imagePath);

    void showAlbums(List<AlbumItemData> albumItemDataList);
}
