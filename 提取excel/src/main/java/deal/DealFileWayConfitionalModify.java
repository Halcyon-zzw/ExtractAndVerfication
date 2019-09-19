package deal;

import java.util.HashMap;
import java.util.List;

/**
 * TODO
 *
 * @Author: zhuzw
 * @Date: 2019/9/10 9:09
 * @Version: 1.0
 */
public interface DealFileWayConfitionalModify {
    /**
     * 从文件中提取分类未confitionString的count条数据
     *
     * @param filePath 文件路径
     * @param conditionMap 条件map，
     *                     其中key为提取的分类
     *                     value为提取的数量
     * @return 结果map
     *      key为提取的分类
     *      value为提取的内容集合
     */
    HashMap<String, List<String>> extractedValue(String filePath, HashMap<String, Integer> conditionMap);
}
