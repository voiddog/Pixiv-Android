package org.voiddog.lib.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.reflect.Field;

/**
 * Created by qigengxin on 16/8/24.
 */
public class ViewUtil {
    /**
     * 根据view获得期望的高度
     *
     * @param view
     * @return
     */
    public static int getExpectHeight(View view) {
        int height = 0;
        if (view == null) return 0;

        final ViewGroup.LayoutParams params = view.getLayoutParams();
        //如果是WRAP_CONTENT，此时图片还没加载，getWidth根本无效
        if (params != null && params.height != ViewGroup.LayoutParams.WRAP_CONTENT) {
            height = view.getWidth(); // 获得实际的宽度
        }
        if (height <= 0 && params != null) {
            height = params.height; // 获得布局文件中的声明的宽度
        }

        if (height <= 0) {
            height = getImageViewFieldValue(view, "mMaxHeight");// 获得设置的最大的宽度
        }

        return height;
    }

    /**
     * 根据view获得期望的宽度
     *
     * @param view
     * @return
     */
    public static int getExpectWidth(View view) {
        int width = 0;
        if (view == null) return 0;

        final ViewGroup.LayoutParams params = view.getLayoutParams();
        //如果是WRAP_CONTENT，此时图片还没加载，getWidth根本无效
        if (params != null && params.width != ViewGroup.LayoutParams.WRAP_CONTENT) {
            width = view.getWidth(); // 获得实际的宽度
        }
        if (width <= 0 && params != null) {
            width = params.width; // 获得布局文件中的声明的宽度
        }

        if (width <= 0) {
            width = getImageViewFieldValue(view, "mMaxWidth");// 获得设置的最大的宽度
        }
        return width;
    }

    /**
     * 获取到距离窗口左侧的偏移量
     * @param view
     * @return
     */
    public static int getRawX(View view){
        int res = 0;
        while(view != null
                && view.getId() != android.R.id.content){
            res += view.getX();
            if(view.getParent() instanceof View) {
                view = (View) view.getParent();
            }
            else{
                break;
            }
        }
        return res;
    }

    /**
     * 获取到距离窗口顶部的偏移量
     * @param view
     * @return
     */
    public static int getRawY(View view){
        int res = 0;
        while(view != null
                && view.getId() != android.R.id.content){
            res += view.getY();
            if(view.getParent() instanceof View) {
                view = (View) view.getParent();
            }
            else{
                break;
            }
        }
        return res;
    }

    /**
     * 通过反射获取imageview的某个属性值
     *
     * @param object
     * @param fieldName
     * @return
     */
    private static int getImageViewFieldValue(Object object, String fieldName) {
        int value = 0;
        try {
            Field field = ImageView.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            int fieldValue = field.getInt(object);
            if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE) {
                value = fieldValue;
            }
        } catch (Exception ignore) {
        }
        return value;
    }
}
