package util;

import org.apache.poi.ss.formula.functions.T;

import java.util.*;

/**
 * TODO
 *
 * @Author: zhuzw
 * @Date: 2019/11/28 14:49
 * @Version: 1.0
 */
public class MapUtil<K, V> {

    /**
     * Map按值排序
     * @param oriMap 原始map
     * @param comparator 比较器
     * @return
     */
    public Map<K, V> sortMapByValue(Map<K, V> oriMap, Comparator comparator) {
        Map<K, V> sortedMap = new LinkedHashMap<K, V>();
        if (oriMap != null && !oriMap.isEmpty()) {
            List<Map.Entry<K, V>> entryList = new ArrayList<Map.Entry<K, V>>(oriMap.entrySet());

            Collections.sort(entryList, comparator);

            Iterator<Map.Entry<K, V>> iter = entryList.iterator();
            Map.Entry<K, V> tmpEntry = null;
            while (iter.hasNext()) {
                tmpEntry = iter.next();
                sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
            }
        }
        return sortedMap;
    }
}
