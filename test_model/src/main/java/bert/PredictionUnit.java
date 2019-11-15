package bert;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 单个预测结果
 *
 * @Author: zhuzw
 * @Date: 2019/8/26 14:34
 * @Version: 1.0
 */
@AllArgsConstructor
@Data
public class PredictionUnit {
  /**
   * 标签
   */
  private String label;
  /**
   * 预测值
   */
  private Double score;
}
