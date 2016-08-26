package org.voiddog.lib.util;

import android.util.Log;

/**
 * log工具类
 * Created by Dog on 2015/5/16.
 */
public class LogUtil {
    public static boolean debug = false;
    public static String TAG = "DOG_TAG";

    public static void I(String i){
        I(TAG, i);
    }

    public static void I(String tag, String i){
        if(debug){
            Log.i(tag, i);
        }
    }

    public static void E(String e){
        E(TAG, e);
    }

    public static void E(String tag, String e){
        if(debug){
            Log.e(tag, e);
        }
    }

    public static void D(String d){
        D(TAG, d);
    }

    public static void D(String tag, String d){
        if(debug){
            Log.d(tag, d);
        }
    }
}
