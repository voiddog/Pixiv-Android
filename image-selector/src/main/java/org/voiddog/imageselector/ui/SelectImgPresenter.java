package org.voiddog.imageselector.ui;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import org.voiddog.imageselector.data.AlbumItemData;
import org.voiddog.imageselector.data.ImageFolder;
import org.voiddog.imageselector.data.ImageInfo;
import org.voiddog.lib.mvp.MvpBasePresenter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 图片选择P
 * Created by qigengxin on 16/9/7.
 */
public class SelectImgPresenter extends MvpBasePresenter<SelectImgMvpView>{

    Context context;
    List<ImageFolder> imageFolders;

    public SelectImgPresenter(Context context){
        this.context = context;
    }

    /**
     * 初始化扫描图库
     */
    public void startScanImgs(){
        showImages(Observable.create(new Observable.OnSubscribe<ImageFolder>() {
            @Override
            public void call(Subscriber<? super ImageFolder> subscriber) {
                imageFolders = scanImageFolders();
                for(ImageFolder folder : imageFolders){
                    subscriber.onNext(folder);
                }
                subscriber.onCompleted();
            }
        }), "图库");
    }

    /**
     * 显示相册内容
     */
    public void showAlbum(AlbumItemData data){
        showImages(Observable.from(data.imageFolderList), data.albumName);
    }

    /**
     * 显示所有的相册
     */
    public void showAlbumList(){
        if(!isViewAttached()){
            return;
        }

        if(imageFolders.size() == 0){
            getView().showError("空空如也");
            return;
        }

        List<AlbumItemData> albumItemDataList = new ArrayList<>();
        AlbumItemData all = new AlbumItemData();
        all.albumName = "图库";
        all.imageFolderList = new ArrayList<>();
        all.imageFolderList.addAll(imageFolders);
        all.fistImagePath = imageFolders.get(0).getFirstImagePath();
        all.imageCount = 0;
        albumItemDataList.add(all);

        for(ImageFolder imageFolder : imageFolders){
            AlbumItemData data = new AlbumItemData();
            data.fistImagePath = imageFolder.getFirstImagePath();
            data.imageFolderList = new ArrayList<>();
            data.imageFolderList.add(imageFolder);
            data.albumName = imageFolder.getName();
            data.imageCount = imageFolder.getImageInfoList().size();
            all.imageCount += data.imageCount;
            albumItemDataList.add(data);
        }

        getView().showAlbums(albumItemDataList);
    }

    /**
     * 扫描图片并显示
     * @param observable 图片源
     * @param showTitle 要显示的标题, 比如图库
     */
    void showImages(Observable<ImageFolder> observable, final String showTitle){
        final List<ImageInfo> imageInfoList = new ArrayList<>();

        observable.flatMap(new Func1<ImageFolder, Observable<ImageInfo>>() {
            @Override
            public Observable<ImageInfo> call(ImageFolder imageFolder) {
                return Observable.from(imageFolder.getImageInfoList());
            }
        }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<ImageInfo>() {
            @Override
            public void onCompleted() {
                if(isViewAttached()){
                    Collections.sort(imageInfoList, new CompareImages());
                    List<String> imgPathList = new ArrayList<>();
                    for(ImageInfo imageInfo : imageInfoList){
                        imgPathList.add(imageInfo.path);
                    }
                    getView().showImages(showTitle, imgPathList);
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if(isViewAttached()){
                    getView().showError("图片读取错误");
                }
            }

            @Override
            public void onNext(ImageInfo imageInfo) {
                imageInfoList.add(imageInfo);
            }
        });
    }

    /**
     * 扫描图片
     * @return 返回扫描后的图片文件夹列表
     */
    List<ImageFolder> scanImageFolders(){
        List<ImageFolder> ret = new ArrayList<>();

        Uri imageUrl = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = context.getContentResolver();

        // 查询jpg和png的图片
        Cursor cursor = contentResolver.query(imageUrl, null,
                MediaStore.Images.Media.MIME_TYPE + "=? or "
                        + MediaStore.Images.Media.MIME_TYPE + "=?",
                new String[] {"image/jpeg", "image/png"},
                MediaStore.Images.Media.DATE_ADDED + " DESC");

        if(cursor == null){
            return ret;
        }

        Map<String, ImageFolder> scanedDir = new HashMap<>();

        while(cursor.moveToNext()){
            // 获取图片的路径
            ImageInfo imageInfo = new ImageInfo();
            imageInfo.path = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Images.Media.DATA));
            imageInfo.dateAdded = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
            imageInfo.dateModified = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED));
            imageInfo.size = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Images.Media.SIZE));

            // 获取该图片的父路径名
            File parentFile = new File(imageInfo.path).getParentFile();
            if (parentFile == null) {
                continue;
            }
            String dirPath = parentFile.getAbsolutePath();
            ImageFolder imageFolder;
            if (scanedDir.containsKey(dirPath)){
                imageFolder = scanedDir.get(dirPath);
            }
            else{
                // 初始化image Folder
                imageFolder = new ImageFolder();
                imageFolder.setDir(dirPath);
                imageFolder.setFirstImagePath(imageInfo.path);
                scanedDir.put(dirPath, imageFolder);
                ret.add(imageFolder);
            }
            imageFolder.addImageInfo(imageInfo);
        }

        cursor.close();
        Collections.sort(ret, new CompareFolder());
        return ret;
    }

    /**
     * 文件夹根据图片数排序
     */
    class CompareFolder implements Comparator<ImageFolder> {

        @Override
        public int compare(ImageFolder lhs, ImageFolder rhs) {
            if(lhs.getCount() == rhs.getCount()){
                return 0;
            }
            else if(lhs.getCount() < rhs.getCount()){
                return  1;
            }
            return -1;
        }
    }

    class CompareImages implements Comparator<ImageInfo>{

        @Override
        public int compare(ImageInfo o1, ImageInfo o2) {
            if(o1.dateAdded > o2.dateAdded){
                return -1;
            }
            else if(o1.dateAdded < o2.dateAdded){
                return 1;
            }
            return 0;
        }
    }
}
