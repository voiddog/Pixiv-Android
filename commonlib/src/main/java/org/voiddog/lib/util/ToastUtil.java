package org.voiddog.lib.util;

import android.content.Context;
import android.widget.Toast;

/**
 * 显示Toast工具类
 * Created by Dog on 2015/4/4.
 */
public class ToastUtil {

    public static void toastShort(Context context, String message){
        if(context != null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    public static void toastLong(Context context, String message){
        if(context != null) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }
}
