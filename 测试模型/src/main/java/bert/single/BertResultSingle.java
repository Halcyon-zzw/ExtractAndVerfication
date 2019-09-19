package bert.single;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * bert结果数据对象
 *
 * @Author: zhuzw
 * @Date: 2019/8/30 15:49
 * @Version: 1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BertResultSingle {
    /**
     * 标签
     */
    private String label;

    /**
     * 预测概率
     */
    private float score;
}
