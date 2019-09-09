package com.koloce.kulibrary.utils.http.encryption;

import java.util.Comparator;

/**
 * Created by koloces on 2019/9/9
 */
public class MapKeyComparator implements Comparator<String> {


    @Override
    public int compare(String str1, String str2) {
        return str1.compareTo(str2);
    }
}
