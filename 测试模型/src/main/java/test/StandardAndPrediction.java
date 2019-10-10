package test;

import lombok.Data;

/**
 * 标准情感类别数据 & 预测情感类别数据
 *
 * @Author: zhuzw
 * @Date: 2019/10/10 8:57
 * @Version: 1.0
 */
@Data
public class StandardAndPrediction {
    private String standard;
    private String prediction;
}
