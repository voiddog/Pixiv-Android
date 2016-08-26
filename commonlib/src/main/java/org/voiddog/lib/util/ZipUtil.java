package org.voiddog.lib.util;

import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.Inflater;

/**
 * 数据压缩类
 * Created by Dog on 2015/5/8.
 */
public class ZipUtil {
    public static byte[] zipByte(byte[] data) {
        Deflater compresser = new Deflater();
        compresser.reset();
        compresser.setInput(data);
        compresser.finish();
        byte result[] = new byte[0];
        ByteArrayOutputStream o = new ByteArrayOutputStream(1);
        try{
            byte[] buf = new byte[1024];
            int got;
            while (!compresser.finished()){
                got = compresser.deflate(buf);
                o.write(buf, 0, got);
            }

            result = o.toByteArray();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            try{
                o.close();
            }
            catch (IOException e){
                e.printStackTrace();
            }
            compresser.end();
        }
        return result;
    }

    public static byte[] unZipByte(byte[] data){
        if(data.length<=0) {
            return new byte[0];
        }
        Inflater decompresser = new Inflater();
        decompresser.setInput(data);
        byte result[] = new byte[0];
        ByteArrayOutputStream o = new ByteArrayOutputStream(1);
        try{
            byte[] buf = new byte[1024];
            int got = 0;
            while (!decompresser.finished()){
                got = decompresser.inflate(buf);
                o.write(buf, 0, got);
            }
            result = o.toByteArray();
        }
        catch (Exception | OutOfMemoryError e){
            e.printStackTrace();
        } finally{
            try{
                o.close();
            }
            catch (IOException e){
                e.printStackTrace();
            }
            decompresser.end();
        }
        return result;
    }

    public static String base64Decode(String inputString) {
        return new String(Base64.decode(inputString, Base64.DEFAULT));
    }

    public static byte[] gzip(byte[] original) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(original.length);
        GZIPOutputStream gos = null;
        try {
            gos = new GZIPOutputStream(bos);
            gos.write(original, 0, original.length);
            gos.finish();
            //gos.flush();
            /**
             * 在android 4.4版本GZIPOutputStream的实现中
             * 构造函数将syncFlush设为了true,其余版本此值都为false
             * true的情况下调用flush会有一个bug,详情见
             * http://115.28.77.87/apis/?p=245
             * https://code.google.com/p/android/issues/detail?id=62589
             */
            bos.flush();
            original = bos.toByteArray();
        } finally {
            closeQuietly(bos);
            closeQuietly(gos);
        }
        return original;
    }

    public static byte[] ungzip(byte[] buf) throws IOException {
        GZIPInputStream gzi = null;
        ByteArrayOutputStream bos = null;
        try {
            gzi = new GZIPInputStream(new ByteArrayInputStream(buf));
            bos = new ByteArrayOutputStream(buf.length);
            int count = 0;
            byte[] tmp = new byte[2048];
            while ((count = gzi.read(tmp)) != -1) {
                bos.write(tmp, 0, count);
            }
            buf = bos.toByteArray();
        } finally {
            closeQuietly(bos);
            closeQuietly(gzi);
        }
        return buf;
    }

    private static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException ioe) {
            // ignore
        }
    }
}
