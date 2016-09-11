package org.voiddog.imageselector.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 图片信息数据
 * Created by qigengxin on 16/9/8.
 */
public class ImageInfo implements Parcelable{
    // 图片路径
    public String path;
    // 添加日期
    public long dateAdded;
    // 修改日期
    public long dateModified;
    // 大小 in bytes
    public long size;

    public ImageInfo(){}

    protected ImageInfo(Parcel in) {
        path = in.readString();
        dateAdded = in.readLong();
        dateModified = in.readLong();
        size = in.readLong();
    }

    public static final Creator<ImageInfo> CREATOR = new Creator<ImageInfo>() {
        @Override
        public ImageInfo createFromParcel(Parcel in) {
            return new ImageInfo(in);
        }

        @Override
        public ImageInfo[] newArray(int size) {
            return new ImageInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(path);
        dest.writeLong(dateAdded);
        dest.writeLong(dateModified);
        dest.writeLong(size);
    }
}
