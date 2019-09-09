package com.koloce.kulibrary.utils.city;

import android.util.ArrayMap;

import com.google.gson.Gson;
import com.koloce.kulibrary.base.BaseApp;
import com.koloce.kulibrary.utils.StringUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by koloces on 2019/6/1
 */
public class CityDataManager {


    private static List<AddressBean> ALL_ADDRESS;

    /**
     * ASCII 排序的省
     */
    private static ArrayMap<Character, List<AddressBean>> PROVINCES_ASCII;

    /**
     * 所有县
     */
    public static ArrayMap<Integer, AddressBean> ALL_DISTRICT = new ArrayMap<>();

    /**
     * 所有地区的根据ID的map,方便查找(这里只存一二级的,第三级不需要)
     */
    public static ArrayMap<Integer, AddressBean> ALL_ADDRESS_MAP = new ArrayMap<>();
    /**
     * 省
     */
    public static List<AddressBean> PROVINCES_LIST = new ArrayList<>();
    /* 省 MAP*/
//    public static ArrayMap<Integer, AddressBean> PROVINCES_MAP = new ArrayMap<>();
    /**
     * 市
     */
    public static ArrayMap<Integer, List<AddressBean>> CITYS = new ArrayMap<>();
    /**
     * 县
     */
    public static ArrayMap<Integer, List<AddressBean>> COUNTIES = new ArrayMap<>();

    public static void init() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getDatas();
            }
        }).start();
    }

    private static void getDatas() {
        try {
            // 读取assets目录里的test.json文件，获取字节输入流
            InputStream is = BaseApp.getContext().getResources().getAssets().open("address.json");
            // 获取字节输入流长度
            int length = is.available();
            // 定义字节缓冲区
            byte[] buffer = new byte[length];
            // 读取字节输入流，存放到字节缓冲区里
            is.read(buffer);
            // 将字节缓冲区里的数据转换成utf-8字符串
            String addressStr = new String(buffer, "utf-8");
            ALL_ADDRESS = new Gson().fromJson(addressStr, CitysBean.class).RECORDS;
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            getCityMaps();
        }
    }

    private static void getCityMaps() {
        if (ALL_ADDRESS == null) return;
        for (AddressBean address : ALL_ADDRESS) {
            if ("1".equals(address.level)) {
                ALL_ADDRESS_MAP.put(Integer.parseInt(address.id), address);
                PROVINCES_LIST.add(address);
            } else if ("2".equals(address.level)) {
                ALL_ADDRESS_MAP.put(Integer.parseInt(address.id), address);
                List<AddressBean> addressBeans = CITYS.get(Integer.parseInt(address.pid));
                if (addressBeans == null) {
                    addressBeans = new ArrayList<>();
                }
                addressBeans.add(address);
                CITYS.put(Integer.parseInt(address.pid), addressBeans);
            } else if ("3".equals(address.level)) {
                ALL_DISTRICT.put(Integer.parseInt(address.id), address);
                List<AddressBean> addressBeans = COUNTIES.get(Integer.parseInt(address.pid));
                if (addressBeans == null) {
                    addressBeans = new ArrayList<>();
                }
                addressBeans.add(address);
                COUNTIES.put(Integer.parseInt(address.pid), addressBeans);
            }
        }
        /* 使用完置为空 */
        ALL_ADDRESS = null;
    }

    /**
     * 获取区
     *
     * @param districtId
     * @return
     */
    public static AddressBean getDistrict(String districtId) {
        if (StringUtil.isEmpty(districtId)) return null;
        try {
            int id = Integer.parseInt(districtId);
            return ALL_DISTRICT.get(id);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据区域名字获取区
     *
     * @param str
     * @return
     */
    public static AddressBean getDistrictFromName(String str) {
        if (StringUtil.isEmpty(str) || "未知".equals(str)) {
            return null;
        }
        Iterator<Map.Entry<Integer, AddressBean>> iterator = ALL_DISTRICT.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, AddressBean> next = iterator.next();
            AddressBean value = next.getValue();

            if(value == null){
                return null;
            }
            if (StringUtil.isEmpty(value.name)){
                return null;
            }
            if (value.name.contains(str) || str.contains(value.name)) {
                return value;
            }
        }
        return null;
    }

    /**
     * 获取父地区
     *
     * @param pid 父级id
     * @return 空为没有父级
     */
    public static AddressBean getParent(String pid) {
        if ("0".equals(pid)) {
            return null;
        }
        return ALL_ADDRESS_MAP.get(Integer.parseInt(pid));
    }

    /**
     * 获取全部省份
     *
     * @return
     */
    public static List<AddressBean> getProvinces() {
        return PROVINCES_LIST;
    }

    /**
     * 获取pid相应的城市
     *
     * @param pid
     * @return
     */
    public static List<AddressBean> getCitys(String pid) {
        return CITYS.get(Integer.parseInt(pid));
    }

    /**
     * 获取pid相应的区县
     *
     * @param pid
     * @return
     */
    public static List<AddressBean> getCounties(String pid) {
        return COUNTIES.get(Integer.parseInt(pid));
    }

    /**
     * 根据文字或拼音查询位置(这里只查询省市级,区级太小不作查询)
     *
     * @param tag
     * @return
     */
    public static AddressBean selectedAddress(String tag) {
        boolean isPingYing = false;
        if (StringUtil.isEnglish(tag)) {
            isPingYing = true;
            //先转小写
            tag = tag.toLowerCase();
        }
        AddressBean result = null;
        AddressBean value = null;
        Iterator<Map.Entry<Integer, AddressBean>> iterator = ALL_ADDRESS_MAP.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, AddressBean> next = iterator.next();
            value = next.getValue();
            if (isPingYing) {
                if (value.pinyin.equals(tag)) {
                    result = value;
                    break;
                }
            } else {
                if (value.name.equals(tag) || value.shortname.equals(tag)) {
                    result = value;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 获取省的首字母ASCII排序
     *
     * @return
     */
    public static ArrayMap<Character, List<AddressBean>> getProvincesAscii() {
        if (PROVINCES_ASCII == null) {
            PROVINCES_ASCII = new ArrayMap<>();
            for (AddressBean addressBean : PROVINCES_LIST) {
                List<AddressBean> beans = PROVINCES_ASCII.get(addressBean.first.charAt(0));
                if (beans == null) {
                    beans = new ArrayList<>();
                }
                beans.add(addressBean);
                PROVINCES_ASCII.put(addressBean.first.charAt(0), beans);
            }
        }
        return PROVINCES_ASCII;
    }

    /**
     * 获取父布局对应的区县类
     *
     * @param parentName 父亲城市
     * @return
     */
    public static AddressBean getCountry(String parentName, String countryName) {
        AddressBean value;
        AddressBean result = null;
        Iterator<Map.Entry<Integer, AddressBean>> iterator = ALL_ADDRESS_MAP.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, AddressBean> next = iterator.next();
            value = next.getValue();
            if (value.name.equals(parentName) || value.shortname.equals(parentName)) {
                if (value.level.equals("2")) {
                    for (AddressBean addressBean : getCounties(value.id)) {
                        if (addressBean == null) {
                            break;
                        }

                        if (addressBean.name.equals(countryName) || addressBean.shortname.equals(countryName)) {
                            result = addressBean;
                        }
                    }
                    break;
                }
            }
        }
        return result;
    }

    private class CitysBean {
        List<AddressBean> RECORDS;
    }
}
