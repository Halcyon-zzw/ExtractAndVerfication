package model;

import lombok.Data;

/**
 * 提取数据结果类型：label + title + content
 *
 * @Author: zhuzw
 * @Date: 2019/9/11 10:46
 * @Version: 1.0
 */
@Data
public class LabelAndCount {
    private int label;


    private Integer count;

    public LabelAndCount(int label, Integer article) {
        this.label = label;
        this.count = article;
    }

    @Override
    public String toString() {
        return String.valueOf(label) + ':' + count;
    }
}
