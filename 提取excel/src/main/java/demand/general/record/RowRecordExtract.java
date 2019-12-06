package demand.general.record;

import java.io.IOException;
import java.util.List;

/**
 * 行数据集合获取接口
 *
 * @Author: zhuzw
 * @Date: 2019/11/17 1:09
 * @Version: 1.0
 */
public interface RowRecordExtract {
    /**
     * 初始化，用于创建
     */
    void init(String path);
    List<String[]> getRawRecords() throws IOException;
    List<String[]> getRawRecords(int count) throws IOException;
}
