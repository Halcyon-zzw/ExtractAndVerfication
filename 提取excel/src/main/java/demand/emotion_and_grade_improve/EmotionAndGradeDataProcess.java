package demand.emotion_and_grade_improve;

import demand.general.RowValueProcess;

/**
 * 情感7分类数据处理
 *
 * @Author: zhuzw
 * @Date: 2019/9/23 16:36
 * @Version: 1.0
 */
public class EmotionAndGradeDataProcess extends RowValueProcess {

    /**
     * 默认直接使用标签；需计算重写该方法
     * @param labels
     * @return
     */
    @Override
    public String getLabel(String... labels) {

        int label_1 = Integer.parseInt(labels[0]);
        int label_2 = Integer.parseInt(labels[1]);
        //计算label
        int label = (label_1 - 1) * 3 + label_2;
        //中性4、5、6 统一成 4
        if (label > 3 && label <7) {
            label = 5;
        }

        return String.valueOf(label);
    }
}
