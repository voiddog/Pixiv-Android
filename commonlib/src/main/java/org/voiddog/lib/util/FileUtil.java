package org.voiddog.lib.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 文件工具类
 * Created by Dog on 2015/5/15.
 */
public class FileUtil {

    /**
     * 获取本地文件夹路径
     * @param dir 文件夹名
     * @return 文件夹的完整路径
     */
    public static String getLocalStorePath(Context context, String dir) {
        String path;

        if (isExternalStorageExist() && getSDFreeSize() >= 10) {
            path = context.getExternalCacheDir().getPath() + "/" + dir + "/";
        } else {
            path = context.getCacheDir().getPath() + "/" + dir + "/";
        }

        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }

        return path;
    }

    /**
     * 判断SD卡是否存在
     *
     * @return boolean
     */
    public static boolean ExistSDCard() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    public static byte[] getBytesFromFile(File f){
        if (f == null){
            return null;
        }
        try{
            FileInputStream stream = new FileInputStream(f);
            ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = stream.read(b)) != -1)
                out.write(b, 0, n);
            stream.close();
            out.close();
            return out.toByteArray();
        } catch (IOException ignore){
        }
        return null;
    }

    /**
     * 判断有无外部存储
     * @return true 存在，false 不存在
     */
    public static boolean isExternalStorageExist(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 判断SD卡剩余容量，单位MB
     *
     * @return long
     */
    public static long getSDFreeSize() {
        // 取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        // 获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        // 空闲的数据块的数量
        long freeBlocks = sf.getAvailableBlocks();
        // 返回SD卡空闲大小
        return (freeBlocks * blockSize) / 1024 / 1024; // 单位MB
    }

    /**
     * 通过Uri获取File
     * @param context 上下文
     * @param uri uri地址
     * @return file文件对象
     */
    public static String getFilePathByUri(Context context, Uri uri){
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(context, uri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    /**
     * 删除一个人文件夹
     * @param file 文件夹file对象
     */
    public static void dfsToDeleteDir(File file){
        if(file.isFile()){
            file.delete();
            return;
        }

        if(file.isDirectory()){
            File[] childFiles = file.listFiles();
            if(childFiles == null || childFiles.length == 0){
                file.delete();
                return;
            }

            for (File childFile : childFiles) {
                dfsToDeleteDir(childFile);
            }

            file.delete();
        }
    }
}
