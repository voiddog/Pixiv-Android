package org.voiddog.imageselector.data;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片文件夹Bean
 * 用来存储当前文件夹的路径，当前文件夹包含多少张图片，以及第一张图片路径用于做文件夹的图标
 * Created by qigengxin on 16/9/7.
 */
public class ImageFolder {
    /**
     * 图片的文件夹路径
     */
    private String dir;

    /**
     * 第一张图片的路径
     */
    private String firstImagePath;

    /**
     * 文件夹的名称
     */
    private String name;

    /**
     * 图片信息
     */
    private List<ImageInfo> imageInfos = new ArrayList<>();

    /**
     * 获取文件夹路径
     * @return 文件夹绝对路径名
     */
    public String getDir(){
        return dir;
    }

    /**
     * 设置文件夹路径以及文件名
     * @param dir 文件夹的绝对路径
     */
    public void setDir(String dir){
        this.dir = dir;
        int lastIndexOf = this.dir.lastIndexOf("/");
        this.name = this.dir.substring(lastIndexOf+1);
    }

    /**
     * 获取第一张图片的路径
     * @return 第一张图片的路劲
     */
    public String getFirstImagePath(){
        return firstImagePath;
    }

    /**
     * 设置第一张图片的路径
     * @param firstImagePath 第一张图片的路径
     */
    public void setFirstImagePath(String firstImagePath){
        this.firstImagePath = firstImagePath;
    }

    /**
     * 获取文件夹的名字
     * @return 文件及名
     */
    public String getName(){
        return name;
    }

    /**
     * 获取文件夹下图片的数量
     * @return 图片的数量
     */
    public int getCount(){
        return imageInfos.size();
    }

    public void addImageInfo(ImageInfo imageInfo){
        imageInfos.add(imageInfo);
    }

    public List<String> getAbsImagePathList(){
        List<String> ret = new ArrayList<>();
        for(ImageInfo imageInfo : imageInfos){
            ret.add(imageInfo.path);
        }
        return ret;
    }

    public List<ImageInfo> getImageInfoList(){
        return imageInfos;
    }
}
