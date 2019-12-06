package bert;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

/**
 * 多分类相应结果
 *
 * @Author: zhuzw
 * @Date: 2019/9/12 15:47
 * @Version: 1.0
 */
@Data
public class BertResultMultiple {
    /**
     * 预测结果集
     */
    private List<PredictionUnit> predictionList;

}
