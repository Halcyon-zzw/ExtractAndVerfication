package deal;

import java.util.List;

/**
 * 有条件的处理文件接口
 *
 * @Author: zhuzw
 * @Date: 2019/8/19 11:08
 * @Version: 1.0
 */
public interface DealFileWayConditional {

    /**
     * 从文件中提取分类未confitionString的count条数据
     *
     * @param filePath 文件路径
     * @param conditionString 分类类别
     * @param count 提取的数据量
     * @return
     */
    List<String> extractedValue(String filePath, String conditionString, int count);
}
