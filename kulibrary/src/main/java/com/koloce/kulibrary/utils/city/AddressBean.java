package com.koloce.kulibrary.utils.city;



import com.koloce.kulibrary.view.wheelview.IWheelEntity;

import java.util.Objects;

import me.yokeyword.indexablerv.IndexableEntity;

/**
 * Created by koloces on 2019/6/1
 */
public class AddressBean implements IndexableEntity, IWheelEntity {

    /**
     * id : 1
     * pid : 0
     * shortname : 北京
     * name : 北京
     * mergename : 中国,北京
     * level : 1
     * pinyin : beijing
     * code :
     * zip :
     * first : B
     * lng : 116.405285
     * lat : 39.904989
     * hot : 1
     */

    public String id;
    public String pid;
    public String shortname;
    public String name;
    public String mergename;
    public String level;
    public String pinyin;
    public String code;
    public String zip;
    public String first;
    public String lng;
    public String lat;
    public String hot;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        AddressBean that = (AddressBean) object;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, pid, shortname, name, mergename, level, pinyin, code, zip, first, lng, lat, hot);
    }

    @Override
    public String toString() {
        return "==id==>" + id + "\n" +
                "==pid==>" + pid + "\n" +
                "==shortname==>" + shortname + "\n" +
                "==name==>" + name + "\n" +
                "==mergename==>" + mergename + "\n" +
                "==level==>" + level + "\n" +
                "==pinyin==>" + pinyin + "\n" +
                "==code==>" + code + "\n" +
                "==zip==>" + zip + "\n" +
                "==first==>" + first + "\n" +
                "==lng==>" + lng + "\n" +
                "==lat==>" + lat + "\n" +
                "==hot==>" + hot + "\n";
    }

    @Override
    public String getFieldIndexBy() {
        return name;
    }

    @Override
    public void setFieldIndexBy(String indexField) {
        this.name = indexField;
    }

    @Override
    public void setFieldPinyinIndexBy(String pinyin) {
        this.pinyin = pinyin;
    }

    @Override
    public String getWheelText() {
        return name;
    }
}
