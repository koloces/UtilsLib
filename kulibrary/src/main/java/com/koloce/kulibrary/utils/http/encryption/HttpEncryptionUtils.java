package com.koloce.kulibrary.utils.http.encryption;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 * Created by koloces on 2019/9/9
 */
public class HttpEncryptionUtils {
    private static final String randomString = "12345467890qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
    private static Random random = new Random();

    /**
     * 生成随机字符串16-32位
     *
     * @return
     */
    public static String getRandomStr() {
        StringBuffer sbf = new StringBuffer();
        for (int i = 0, len = random.nextInt(17) + 16; i < len; i++) {
            sbf.append(randomString.charAt(random.nextInt(63)));
        }
        return sbf.toString();
    }

    /**
     * 获取时间戳字符串
     * @return
     */
    public static String getCurrentTimeMillis(){
        return System.currentTimeMillis() / 1000 + "";
    }

    /**
     * 按照Key的ASCII码排序
     * @param map
     * @return
     */
    public static Map<String,String> getSortMap(Map<String,String> map){
        TreeMap<String, String> sortMap = new TreeMap<>(new MapKeyComparator());
        sortMap.putAll(map);
        return sortMap;
    }

    /**
     * 获取map Value值的拼接
     * @param map
     * @return
     */
    public static String getMapValueSplicingStr(Map<String,String> map){
        if (map == null || map.size() == 0)return "";
        StringBuffer sbf = new StringBuffer();
        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, String> next = iterator.next();
            sbf.append("&");
            sbf.append(next.getValue());
        }
        sbf.delete(0,1);
        return sbf.toString();
    }

    /**
     * 获取map key=value 的拼接
     * @param map
     * @return
     */
    public static String getMapSplicingStr(Map<String,String> map){
        if (map == null || map.size() == 0)return "";
        StringBuffer sbf = new StringBuffer();
        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, String> next = iterator.next();
            sbf.append("&");
            sbf.append(next.getKey());
            sbf.append("=");
            sbf.append(next.getValue());
        }
        sbf.delete(0,1);
        return sbf.toString();
    }

    /**
     * 获取加密字符串
     * @return
     */
    public static String getSign(){
        return "";
    }
}
