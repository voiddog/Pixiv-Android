package org.voiddog.lib.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;

/**
 * 图像处理库
 * Created by Dog on 2015/5/1.
 */
public class ImageUtil {

    /**
     * 得到指定大小的controller
     *
     * @param oldController 旧的controller
     * @param uri           图片资源地址
     * @param width         期望宽度
     * @param height        期望高度
     * @return 带缩放的controller
     */
    public static PipelineDraweeController getControllerWithSize(DraweeController oldController,
                                                                 Uri uri, int width, int height) {
        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(new ResizeOptions(width, height))
                .setAutoRotateEnabled(true)
                .build();
        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setOldController(oldController)
                .setImageRequest(imageRequest)
                .build();
        return controller;
    }

    public static byte[] bitmapToArray(Bitmap bitmap) {
        return bitmapToArray(bitmap, 100);
    }

    public static byte[] bitmapToArray(Bitmap bitmap, int qulity) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, qulity, baos);
        return baos.toByteArray();
    }


    /**
     * 位图转drawable
     *
     * @param drawable 资源
     * @return 位图
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : drawable.getBounds().width();
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : drawable.getBounds().height();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    /**
     * 获取从drawable中指定大小的bitmap
     *
     * @param drawable 资源
     * @param width    生成的bitmap的宽
     * @param height   生成的bitmap的高
     * @return bitmap资源
     */
    public static Bitmap drawableToBitmapWithSize(Drawable drawable, int width, int height) {
        Bitmap bitmap = drawableToBitmap(drawable);
        float scale = Math.min(width * 1.0f / bitmap.getWidth(), height * 1.0f / bitmap.getHeight());
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        bitmap.recycle();
        return resizeBmp;
    }

    /**
     * 从一个view中获取bitmap
     *
     * @param view 视图
     * @return 视图的bitmap
     */
    public static Bitmap getBitmapFromView(View view) {
        try {
            // view不能是RelativeLayout
            view.destroyDrawingCache();
            view.setDrawingCacheEnabled(true);
            Bitmap bitmap = view.getDrawingCache(true);
            if (bitmap != null) {
                bitmap = Bitmap.createBitmap(bitmap);
                view.destroyDrawingCache();
            }
            return bitmap;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取本地图片
     *
     * @param filePath  图片路径
     * @param maxWidth  最大宽度
     * @param maxHeight 最大高度
     * @return 图片bitmap 找不到为null
     */
    public static Bitmap getLocalBitmap(String filePath, int maxWidth, int maxHeight) {
        Bitmap bitmap = null;
        try {
            File file = new File(filePath);
            int degree = ImageUtil.readPictureDegree(filePath);
            if (file.exists()) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(filePath, options);
                options.inSampleSize = calculateSimpleSize(options.outWidth, options.outHeight, maxWidth, maxHeight);
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeFile(filePath, options);
                if (degree != 0) {
                    Bitmap bmp = bitmap;
                    bitmap = rotatingImageView(degree, bitmap);
                    bmp.recycle();
                }
            }

            if (bitmap != null
                    && (bitmap.getWidth() > maxWidth
                    || bitmap.getHeight() > maxHeight)) {

                float scaleSize = Math.max(
                        (float) bitmap.getWidth() / maxWidth,
                        (float) bitmap.getHeight() / maxHeight
                );

                Bitmap oldBitmap = bitmap;
                bitmap = Bitmap.createScaledBitmap(
                        oldBitmap,
                        (int) (bitmap.getWidth() / scaleSize),
                        (int) (bitmap.getHeight() / scaleSize),
                        true
                );

                //释放内存
                if (!oldBitmap.isRecycled()) {
                    oldBitmap.recycle();
                }
            }
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 图片存储
     *
     * @param b       bitmap
     * @param outPath 存储的路径
     * @param q       质量
     * @return 存储的文件
     */
    public static File saveImg(Bitmap b, String outPath, int q) {
        File mediaFile = new File(outPath);
        try {
            if (mediaFile.exists()) {
                mediaFile.delete();
            }
            if (!mediaFile.getParentFile().exists()) {
                mediaFile.getParentFile().mkdirs();
            }
            mediaFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(mediaFile);
            b.compress(Bitmap.CompressFormat.JPEG, q, fos);
            fos.flush();
            fos.close();
            b.recycle();
            System.gc();
            return mediaFile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存图片
     *
     * @param b         图片吧资源
     * @param outPath   输出路径
     * @param maxWidth  输出大小
     * @param maxHeight 输出高度
     * @param q         图片质量
     * @return 图片文件地址
     */
    public static File saveImg(Bitmap b, String outPath, int maxWidth, int maxHeight, int q) {
        if (maxWidth < b.getWidth() || maxHeight < b.getHeight()) {
            float simpleSize;
            float scaleWidth = ((float) b.getWidth()) / maxWidth;
            float scaleHeight = ((float) b.getHeight()) / maxHeight;
            simpleSize = scaleWidth > scaleHeight ? scaleWidth : scaleHeight;
            int newWidth = (int) (b.getWidth() / simpleSize);
            int newHeight = (int) (b.getHeight() / simpleSize);
            return saveImg(
                    Bitmap.createScaledBitmap(b, newWidth, newHeight, true),
                    outPath, q
            );
        } else {
            return saveImg(b, outPath, q);
        }
    }

    /**
     * 压缩本地图片，如果有转角自动转正
     */
    public static File compressLocalImage(String path, String outPath, int maxWidth, int maxHeight, int q) {
        return saveImg(getLocalBitmap(path, maxWidth, maxHeight), outPath, q);
    }

    public static int calculateSimpleSize(int srcWidth, int srcHeight,
                                          int reqWidth, int reqHeight) {
        int inSampleSize = 1;

        if (srcHeight > reqHeight || srcWidth > reqWidth) {
            float scaleW = (float) srcWidth / (float) reqWidth;
            float scaleH = (float) srcHeight / (float) reqHeight;

            float sample = scaleW > scaleH ? scaleW : scaleH;
            // 只能是2的次幂
            if (sample < 3)
                inSampleSize = (int) sample;
            else if (sample < 6.5)
                inSampleSize = 4;
            else if (sample < 8)
                inSampleSize = 8;
            else
                inSampleSize = (int) sample;

        }
        return inSampleSize;
    }

    /**
     * 旋转图片
     *
     * @param angle  旋转角度
     * @param bitmap 需要旋转的图片
     * @return Bitmap
     */
    public static Bitmap rotatingImageView(int angle, Bitmap bitmap) {
        try {
            // 旋转图片 动作
            Matrix matrix = new Matrix();
            matrix.postRotate(angle);
            // 创建新的图片
            return Bitmap.createBitmap(bitmap, 0, 0,
                    bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree 旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 图片转base64字符串 *
     *
     * @param bmp 要转的bitmap
     * @param c   转换质量
     */
    public static String BitmapToString(Bitmap bmp, int c) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, c, baos);
            byte[] b = baos.toByteArray();
            return Base64.encodeToString(b, Base64.DEFAULT);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取本地的图片并转为Base64字符串
     *
     * @param filePath 文件路径
     * @param width    期望的图片宽度
     * @param height   期望的图片高度
     * @param c        图片质量
     * @return 转换后的String
     */
    public static String getLocalBitmapString(String filePath, int width, int height, int c) {
        return BitmapToString(getLocalBitmap(filePath, width, height), c);
    }
}