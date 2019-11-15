package bert;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

/**
 * @description: 预测结果集
 * @author: chenyang
 * @create: 2019-05-28
 **/
@Data
@Accessors(chain = true)
public class PredictionList {
  private List<List<String>> pred_label;
  private List<Map<String,Double>> pred_result;
}
