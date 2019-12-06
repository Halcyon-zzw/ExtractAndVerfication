package bert.single;

import bert.PredictionList;
import lombok.Data;

import java.util.List;

/**
 * bert单分类响应结果
 *
 * @Author: zhuzw
 * @Date: 2019/9/3 15:19
 * @Version: 1.0
 */
@Data
public class BertSingleResponse {
    private String id;
    /**
     * 响应结果体
     */
    private List<Prediction> result;
    /**
     * 状态
     */
    private int status;
}
