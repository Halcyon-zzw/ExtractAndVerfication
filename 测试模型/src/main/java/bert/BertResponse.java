package bert;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @description: bert服务返回结果
 * @author: chenyang
 * @create: 2019-05-28
 **/
@Data
@Accessors(chain = true)
public class BertResponse {
    private String id;
    /**
     * 响应结果体
     */
    private List<PredictionList> result;
  /**
   * 状态
   */
  private int status;
}
