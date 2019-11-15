package test;

import lombok.Data;

/**
 * 预测输出数据
 *
 * @Author: zhuzw
 * @Date: 2019/10/10 8:57
 * @Version: 1.0
 */
@Data
public class PredictionData {
    /**
     * 标准标签
     */
    private String standard;
    /**
     * 预测标签
     */
    private String prediction;

    /**
     * 文章内容
     */
    private String content;

    @Override
    public String toString() {
        return standard + "\t" + prediction + "\t" + content;
    }
}
