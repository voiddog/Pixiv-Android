package org.voiddog.lib.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 缓存类, 序列化存储对象与反序列化
 * Created by Dog on 2015/6/9.
 */
public class CacheUtil {

    /**
     * 保存对象数据到本地路径, 对象必须是实现了 Serialize 接口
     * @param object 要保存的对象
     * @param path 保存的路径
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void SerializeObject(Object object, String path) throws IOException{
        ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(
                new File(path)));
        oo.writeObject(object);
        oo.close();
    }

    /**
     * 反序列化对象
     * @param path 序列化对象文件路径
     * @return 反序列化的对象
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public static Object DeserializeObject(String path) throws ClassNotFoundException, IOException{
        Object o;
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
                new File(path)));
        o = ois.readObject();
        return o;
    }
}
