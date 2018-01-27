package org.zaccoding.util;

import java.util.List;

/**
 * @author zacconding
 * @Date 2018-01-28
 * @GitHub : https://github.com/zacscoding
 */
public class CollectionUtil {

    public static <T> boolean equalsList(List<T> list1, List<T> list2) {
        if ((list1 == null && list2 == null)) {
            return true;
        }
        if (list1 == null || list2 == null) {
            return false;
        }
        if (list1.size() != list2.size()) {
            return false;
        }
        for (T t1 : list1) {
            if (list2.indexOf(t1) < 0) {
                return false;
            }
        }
        return true;
    }
}
