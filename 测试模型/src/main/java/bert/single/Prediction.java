package bert.single;

import lombok.Data;

import java.util.List;

/**
 * 预测结果（单分类）
 *
 * @Author: zhuzw
 * @Date: 2019/9/3 15:21
 * @Version: 1.0
 */
@Data
public class Prediction {
    /**
     * 预测标签集（长度为1的列表）
     */
    private List<String> pred_label;
    /**
     * 预测概率（长度为1的列表）
     */
    private List<Double> score;
}
