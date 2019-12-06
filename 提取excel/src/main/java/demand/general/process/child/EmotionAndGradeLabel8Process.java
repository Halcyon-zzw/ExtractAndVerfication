package demand.general.process.child;

import demand.general.process.ArticleProcess;
import demand.general.process.ProcessWay;

/**
 * 情感8分类数据处理，通过label_1,label_2映射得到
 * 负面一星：101
 * 负面二星：102
 * 负面三星：103
 * 中性无：200
 * 中性三星：203
 * 正面一星：301
 * 正面二星：302
 * 正面三星：303
 *
 * @Author: zhuzw
 * @Date: 2019/9/23 16:36
 * @Version: 1.0
 */
public class EmotionAndGradeLabel8Process extends ArticleProcess {
    public EmotionAndGradeLabel8Process(ProcessWay processWay) {
        super(processWay);
    }

    /**
     * 默认直接使用标签；需计算重写该方法
     * @param labels
     * @return
     */
    @Override
    public String getLabel(String... labels) {

        String label_1 = labels[0];
        String label_2 = labels[1];
        int label_1_Value = 0;
        int label_2_Value = 0;
        //计算label
        switch (label_1) {
            case "负面":
                label_1_Value = 1;
                break;
            case "中性":
                label_1_Value = 2;
                break;
            case "正面":
                label_1_Value = 3;
                break;
            default:
                System.out.println("error: " + label_1 + "未匹配！");
                break;
        }
        switch (label_2) {
            case "一星":
                label_2_Value = 1;
                break;
            case "二星":
                label_2_Value = 2;
                break;
            case "三星":
                label_2_Value = 3;
                break;
            default:
                if (!"无".equalsIgnoreCase(label_2)) {
                    System.out.println("error: " + label_2 + "未匹配！");
                }
                label_2_Value = 0;
                break;

        }
//        int label = (label_1_Value - 1) * 3 + label_2_Value;
//        if ("无".equalsIgnoreCase(label_2)) {
//            label = 5;
//        }
        int label = label_1_Value * 100 + label_2_Value;

        return String.valueOf(label);
    }

    public void info() {
        System.out.println(">>>>>>>处理8分类。");
        super.processWay.info();
    }
}
