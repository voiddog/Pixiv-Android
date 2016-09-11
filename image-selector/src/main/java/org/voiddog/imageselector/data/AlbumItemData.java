package org.voiddog.imageselector.data;

import java.util.List;

/**
 * 相册数据
 * Created by qigengxin on 16/9/8.
 */
public class AlbumItemData {
    // 相册名
    public String albumName;
    // 相册包含的图片文件
    public List<ImageFolder> imageFolderList;
    // 显示的第一张图片
    public String fistImagePath;
    // 相册图片数量
    public int imageCount;
}
