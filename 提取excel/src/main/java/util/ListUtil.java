package util;

import demand.mapping.model.MappingResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * TODO
 *
 * @Author: zhuzw
 * @Date: 2019/11/26 11:04
 * @Version: 1.0
 */
public class ListUtil {

    /**
     * 插入有序列表（升序，sort默认升序）
     *
     * TODO String类型优化
     * @return
     */
    public static List<String> insertInAscOrderedList(List<String> list, String str) {
        List<String> resultList = new ArrayList<>();
        int i = 0;
        for (; i < list.size(); i++) {
            if (StringUtils.compare(str, list.get(i)) <= 0) {
                //str小于该位置，插入
                break;
            }
        }
        list.add(i, str);
        return list;
    }

    /**
     * 插入有序列表（降序，sort默认升序）
     *
     * TODO String类型优化
     * @return
     */
    public static List<String> insertInDescOrderedList(List<String> list, String str) {
        List<String> resultList = new ArrayList<>();
        int i = 0;
        for (; i < list.size(); i++) {
            if (StringUtils.compare(str, list.get(i)) >= 0) {
                //str小于该位置，插入
                break;
            }
        }
        list.add(i, str);
        return list;
    }

    /**
     * 检测列表数量是否达到限定数量
     * 达到，返回true;   否则，返回false
     * @param collection
     * @param count
     * @return
     */
    public static boolean countCheck(Collection collection, int count) {
        if (collection.size() < count) {
            return false;
        }
        return true;
    }

    public static boolean isEmpty(Collection collection) {
        if (null == collection || collection.size() == 0) {
            return true;
        }
        return false;
    }

}
