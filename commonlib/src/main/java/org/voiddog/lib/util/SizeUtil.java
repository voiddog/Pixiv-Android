package org.voiddog.lib.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * 尺寸工具类
 * Created by Dog on 2015/4/3.
 */
public class SizeUtil {

    private static DisplayMetrics displayMetrics = null;

    /**
     * 获取屏幕宽度
     * @param context 上下文
     * @return 屏幕宽度 in px
     */
    public static int getScreenWidth(Context context){
        if(displayMetrics == null) {
            displayMetrics = getLocalDisplayMetrics(context);
        }
        return displayMetrics.widthPixels;
    }

    /**
     * 获取屏幕高度
     * @param context 上下文
     * @return 屏幕高度 in px
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static int getScreenHeight(Context context){
        if(displayMetrics == null) {
            displayMetrics = getLocalDisplayMetrics(context);
        }
        return displayMetrics.heightPixels;
    }

    /**
     * 获取当前设备的DisplayMetrics
     * @param context 上下文
     * @return displayMetric
     */
    public static DisplayMetrics getLocalDisplayMetrics(Context context) {
        if(displayMetrics == null) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            displayMetrics = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(displayMetrics);
        }
        return displayMetrics;
    }

    /**
     * 输入dp 转换为 px
     * @param context 上下文
     * @param dp 要转换的dp的数值
     * @return dp 转换成 px 后的数值
     */
    public static int dp2px(Context context, float dp){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5f);
    }
}
