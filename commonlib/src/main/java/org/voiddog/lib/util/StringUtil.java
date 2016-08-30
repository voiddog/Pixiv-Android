package org.voiddog.lib.util;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.Uri;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 * Created by Dog on 2015/5/2.
 */
public class StringUtil {

    //字体缓存
    public static Map<String, Typeface> fontCache;

    /**
     * 检测邮箱地址是否合法
     * @param email 需要判断的邮箱
     * @return true合法 false不合法
     */
    public static boolean isEmail(String email){
        if (null==email || "".equals(email)) return false;
        Pattern p =  Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static String getPathWithArgs(String url){
        return url.replaceFirst("^((https|http|ftp|rtsp|mms)?:\\/\\/)[^/]+/", "");
    }

    /**
     * string产生int
     * @param s 原始字符串
     * @param d 默认数值
     * @return 转化的结果
     */
    public static int parseInt(String s, int d){
        if(s == null){
            return d;
        }
        int res;
        try{
            res = Integer.parseInt(s);
        } catch (NumberFormatException e){
            res = d;
        }
        return res;
    }

    public static double parseDouble(String s, double d){
        try{
            d = Double.parseDouble(s);
        } catch (NumberFormatException ignore){ }
        return d;
    }

    /**
     * 检查手机号码是否合法
     * @param mobiles 要检查的手机号码
     * @return 是否合法(true or false)
     */
    public static boolean isMobileNO(String mobiles){
        Pattern p = Pattern.compile("^(1[3,4,5,7,8][0-9])\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 根据文件路径获取Uri
     * @param filePath 文件路径
     * @return 文件路径的uri
     */
    public static Uri getUriFromFilePath(String filePath){
        return Uri.parse(String.format("file://%s", filePath));
    }

    /**
     * 将long类型时间转换为xxxx-xx-xx形式的数据
     * @param time 时间
     * @return (xxxx)年-(xx)月-(xx)日的形式的数据
     */
    public static String getTimeString(long time){
        Date date = new Date(time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd", Locale.CHINA);
        return simpleDateFormat.format(date);
    }

    public static boolean isEmpty(String s){
        return s == null || s.length() == 0;
    }

    public static boolean isAllSpace(String s){
        for(int i = 0; i < s.length(); i++){
            if(s.charAt(i) != ' '){
                return false;
            }
        }

        return true;
    }

    public static Typeface getFontFace(AssetManager assetManager, String name){
        try{
            if(fontCache == null){
                fontCache = new HashMap<>();
            }
            if(fontCache.containsKey(name)){
                return fontCache.get(name);
            }
            else {
                Typeface typeface = Typeface.createFromAsset(assetManager, name);
                fontCache.put(name, typeface);
                return typeface;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 对字符串进行MD5加密</br>
     * 如果返回为空，则表示加密失败
     * @param s
     * @return
     */
    public static String md5(String s) {
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            byte[] strTemp = s.getBytes("utf-8");
            // 使用MD5创建MessageDigest对象
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (byte b : md) {
                // 将每个数(int)b进行双字节加密
                str[k++] = hexDigits[b >> 4 & 0xf];
                str[k++] = hexDigits[b & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }
}
